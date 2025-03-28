package com.nasdroid.api.websocket.jsonrpc

import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializer
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi

/**
 * A websocket client following [JSON-RPC 2.0](https://www.jsonrpc.org/specification).
 *
 * Before executing any calls, you'll need to [connect] successfully.
 */
@OptIn(ExperimentalAtomicApi::class)
class JsonRpcWebsocketClient {
    private val idCounter = AtomicInt(1)

    private val bootstrapLock = Mutex()

    private val _state = MutableStateFlow<State>(State.Disconnected)

    /**
     * The current state of the client. See [State] for all states.
     */
    val state: StateFlow<State> = _state

    /**
     * Attempts to open a connection to a JSON-RPC server at [url].
     *
     * When the connection succeeds, [state] is updated to [State.Connected], and this function
     * returns. However, if the connection fails, an [IllegalStateException] is thrown and the state
     * is reset.
     *
     * @throws IllegalStateException when the server rejects the connection.
     * @throws java.net.UnknownHostException when the server at [url] was not reachable.
     */
    suspend fun connect(url: String) {
        bootstrapLock.withLock {
            check(state.value is State.Disconnected) { "Cannot connect when already connected or connecting." }
            _state.value = State.Connecting
            try {
                val webSocket = WebsocketKtorClient.webSocketSession(urlString = url)
                _state.value = State.Connected(webSocket)
            } finally {
                // Ensure state is never connecting when we finish
                if (_state.value is State.Connecting) {
                    _state.value = State.Disconnected
                }
            }
        }
    }

    /**
     * Disconnects from the connected session, or does nothing if not already connected.
     *
     * In case the client is connecting when this is called, this will suspend until connection
     * completes.
     */
    suspend fun disconnect() {
        bootstrapLock.withLock {
            when (val currentState = state.value) {
                is State.Connected -> {
                    currentState.webSocketSession.close()
                    _state.value = State.Disconnected
                }
                is State.Connecting -> {
                    error("Attempted to disconnect while connecting. This should be impossible!")
                }
                State.Disconnected -> { /* Already disconnected, no-op */ }
            }
        }
    }

    /**
     * Performs an RPC call on the connected session.
     *
     * @param T The type of data to be returned from the method call.
     * @param P The type of parameters to be sent alongside the method call.
     *
     * @param method The method to call. Check your server documentation for available methods.
     * @param serializer The [KSerializer] used to deserialize the result.
     * @param params A list of optional parameters to send along with the method call.
     * @param paramSerializer The serializer for parameter items.
     *
     * @throws IllegalStateException if the client wasn't connected with [connect] before making the
     * call.
     * @throws MethodCallError if the server returned an error in response to the method call.
     * @throws SerializeError if the client failed to construct the method call. In this
     * situation, nothing was sent to the server.
     * @throws DeserializeError if the client failed to deserialize the method result.
     */
    @OptIn(ExperimentalUuidApi::class, ExperimentalSerializationApi::class)
    suspend fun <T, P> callMethod(
        method: String,
        serializer: KSerializer<T>,
        params: List<P>,
        paramSerializer: KSerializer<P>
    ): T {
        val currentState = state.value
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val message = RequestMessage(idCounter.fetchAndIncrement(), method, params)
        val serializedString = try {
            Json.encodeToString(RequestMessage.serializer(paramSerializer), message)
        } catch (e: SerializationException) {
            throw SerializeError(e)
        }
        currentState.webSocketSession.send(Frame.Text(serializedString))
        val resultFrame = currentState.webSocketSession.incoming.receive()
        val result = try {
            Json.decodeFromStream(RpcResponseSerializer(serializer), resultFrame.data.inputStream())
        } catch (e: SerializationException) {
            throw DeserializeError(e, resultFrame.data.decodeToString())
        }
        when (result) {
            is ResponseError -> throw MethodCallError(result.error.code, result.error.message)
            is ResponseMessage<*> -> return result.result as T
        }
    }

    /**
     * Performs an RPC call on the connected session.
     *
     * @param T The type of data to be returned from the method call.
     *
     * @param method The method to call. Check your server documentation for available methods.
     * @param serializer The [KSerializer] used to deserialize the result.
     *
     * @throws IllegalStateException if the client wasn't connected with [connect] before making the
     * call.
     * @throws MethodCallError if the server returned an error in response to the method call.
     */
    suspend fun <T> callMethod(
        method: String,
        serializer: KSerializer<T>,
    ): T {
        return callMethod(method, serializer, emptyList(), String.serializer())
    }

    /**
     * Encapsulates possible states that [JsonRpcWebsocketClient] might be in.
     */
    sealed interface State {

        /**
         * The client is trying to connect to a server.
         */
        data object Connecting : State

        /**
         * The client successfully connected to the server.
         *
         * @property webSocketSession The internal websocket session powering the client.
         */
        data class Connected(
            internal val webSocketSession: DefaultClientWebSocketSession,
        ) : State

        /**
         * The client is not connected to any session. This is the default state. Call [connect] to
         * get set up.
         */
        data object Disconnected : State
    }
}

private val WebsocketKtorClient = HttpClient {
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
    }

    install(WebSockets) {
        pingInterval = 1.minutes
    }
}

/**
 * Performs an RPC call on the connected session.
 *
 * @param T The type of data to be returned from the method call.
 *
 * @param method The method to call. Check your server documentation for available methods.
 *
 * @throws IllegalStateException if the client wasn't connected with
 * [com.nasdroid.api.websocket.ddp.DdpWebsocketClient.connect] before making the call.
 * @throws MethodCallError if the server returned an error in response to the method call.
 */
suspend inline fun <reified T> JsonRpcWebsocketClient.callMethod(method: String): T {
    return callMethod(method, serializer())
}

/**
 * Performs an RPC call on the connected session.
 *
 * @param T The type of data to be returned from the method call.
 * @param P The type of parameters to be sent alongside the method call.
 *
 * @param method The method to call. Check your server documentation for available methods.
 * @param params A list of optional parameters to send along with the method call.
 *
 * @throws IllegalStateException if the client wasn't connected with
 * [com.nasdroid.api.websocket.ddp.DdpWebsocketClient.connect] before making the call.
 * @throws MethodCallError if the server returned an error in response to the method call.
 */
suspend inline fun <reified T, reified P> JsonRpcWebsocketClient.callMethod(
    method: String,
    params: List<P>
): T {
    return callMethod(method, serializer(), params, serializer())
}
