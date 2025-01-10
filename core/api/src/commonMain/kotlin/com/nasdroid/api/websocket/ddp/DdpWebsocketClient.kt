package com.nasdroid.api.websocket.ddp

import com.nasdroid.api.websocket.ddp.message.AddedBeforeMessage
import com.nasdroid.api.websocket.ddp.message.AddedMessage
import com.nasdroid.api.websocket.ddp.message.ChangedMessage
import com.nasdroid.api.websocket.ddp.message.ConnectMessage
import com.nasdroid.api.websocket.ddp.message.ConnectServerMessage
import com.nasdroid.api.websocket.ddp.message.ConnectServerMessageSerializer
import com.nasdroid.api.websocket.ddp.message.ConnectedMessage
import com.nasdroid.api.websocket.ddp.message.DataManagementServerMessage
import com.nasdroid.api.websocket.ddp.message.FailedMessage
import com.nasdroid.api.websocket.ddp.message.MethodMessage
import com.nasdroid.api.websocket.ddp.message.MovedBeforeMessage
import com.nasdroid.api.websocket.ddp.message.NosubMessage
import com.nasdroid.api.websocket.ddp.message.ReadyMessage
import com.nasdroid.api.websocket.ddp.message.RemovedMessage
import com.nasdroid.api.websocket.ddp.message.ResultMessage
import com.nasdroid.api.websocket.ddp.message.ServerMessage
import com.nasdroid.api.websocket.ddp.message.ServerMessageSerializer
import com.nasdroid.api.websocket.ddp.message.SubMessage
import com.nasdroid.api.websocket.ddp.message.UnsubMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DdpWebsocketClient {
    private val bootstrapLock = Mutex()

    private val _state = MutableStateFlow<State>(State.Disconnected)
    val state: StateFlow<State> = _state

    suspend fun connect(url: String, session: String? = null) {
        bootstrapLock.withLock {
            check(state.value is State.Disconnected) { "Cannot connect when already connected or connecting." }
            _state.value = State.Connecting
            val webSocket = WebsocketKtorClient.webSocketSession(urlString = url)
            webSocket.sendSerialized(ConnectMessage(version = "1", support = listOf("1"), session))
            when (val connectResponse = webSocket.receiveDeserialized<ConnectServerMessage>()) {
                is ConnectedMessage -> {
                    _state.value = State.Connected(webSocket, connectResponse.session)
                }
                is FailedMessage -> {
                    _state.value = State.Disconnected
                    error("Failed to connect to DDP server: $connectResponse")
                }
            }
        }
    }

    suspend fun disconnect() {
        bootstrapLock.withLock {
            when (val currentState = state.value) {
                is State.Connected -> {
                    currentState.webSocketSession.close()
                    _state.value = State.Disconnected
                }
                is State.Connecting -> error("Attempted to disconnect while connecting. This should be impossible!")
                State.Disconnected -> { /* Already disconnected, no-op */ }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalSerializationApi::class)
    suspend fun <T, P> callMethod(
        method: String,
        serializer: KSerializer<T>,
        params: List<P>,
        paramSerializer: KSerializer<P>
    ): T {
        val currentState = state.value
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val id = Uuid.random().toString()
        val message = MethodMessage(id, method, params)
        val serializedString = Json.encodeToString(MethodMessage.serializer(paramSerializer), message)
        currentState.webSocketSession.send(Frame.Text(serializedString))
        val resultFrame = currentState.webSocketSession.incoming.receive()
        val result = Json.decodeFromStream(ResultMessage.serializer(serializer), resultFrame.data.inputStream())
        return if (result.result != null) {
            result.result
        } else {
            checkNotNull(result.error) { "The result of the method call was neither success or error!" }
            throw MethodCallError(
                error = result.error.error,
                errorType = result.error.errorType,
                reason = result.error.reason,
                message = result.error.message
            )
        }
    }

    suspend fun <T> callMethod(
        method: String,
        serializer: KSerializer<T>,
    ): T {
        return callMethod(method, serializer, emptyList(), String.serializer())
    }

    @OptIn(ExperimentalUuidApi::class)
    fun <T, P> subscribe(
        name: String,
        serializer: KSerializer<T>,
        params: List<P>,
        paramSerializer: KSerializer<P>
    ): Flow<SubscriptionEvent<T>> {
        val currentState = state.value
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val id = Uuid.random().toString()
        return currentState.webSocketSession.incoming.receiveAsFlow()
            .map { value ->
                val message = currentState.webSocketSession.converter!!.deserialize<DataManagementServerMessage>(value)
                when (message) {
                    is AddedBeforeMessage<*> -> SubscriptionEvent.DocumentAdded(
                        collection = message.collection,
                        id = message.id,
                        document = message.fields as T,
                        beforeId = message.before
                    )
                    is AddedMessage<*> -> SubscriptionEvent.DocumentAdded(
                        collection = message.collection,
                        id = message.id,
                        document = message.fields as T,
                        beforeId = null
                    )
                    is ChangedMessage -> SubscriptionEvent.DocumentChanged(
                        collection = message.collection,
                        id = message.id,
                        updatedFields = message.fields.orEmpty(),
                        clearedFields = message.cleared.orEmpty()
                    )
                    is MovedBeforeMessage -> SubscriptionEvent.DocumentMoved(
                        collection = message.collection,
                        id = message.id,
                        movedBefore = message.before
                    )
                    is NosubMessage -> throw CancellationException("Server cancelled the subscription")
                    is ReadyMessage -> null // We currently ignore this
                    is RemovedMessage -> SubscriptionEvent.DocumentRemoved(
                        collection = message.collection,
                        id = message.id
                    )
                }
            }
            .filterNotNull()
            .onStart {
                val sub = SubMessage(id, name, params)
                val serializedString = Json.encodeToString(SubMessage.serializer(paramSerializer), sub)
                currentState.webSocketSession.send(Frame.Text(serializedString))
            }
            .onCompletion {
                val unsub = UnsubMessage(id)
                currentState.webSocketSession.sendSerialized(unsub)
                currentState.webSocketSession.receiveDeserialized<NosubMessage>()
            }
    }

    sealed interface State {
        data object Connecting : State

        data class Connected(
            internal val webSocketSession: DefaultClientWebSocketSession,
            internal val sessionId: String
        ) : State

        data object Disconnected : State
    }
}

private val MessageSerializer = Json {
    serializersModule = SerializersModule {
        polymorphic(ServerMessage::class) {
            defaultDeserializer { ServerMessageSerializer }
        }
        polymorphic(ConnectServerMessage::class) {
            defaultDeserializer { ConnectServerMessageSerializer }
        }
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
        contentConverter = KotlinxWebsocketSerializationConverter(MessageSerializer)
        pingInterval = 1.minutes
    }
}

suspend inline fun <reified T> DdpWebsocketClient.callMethod(method: String): T {
    return callMethod(method, serializer())
}

suspend inline fun <reified T, reified P> DdpWebsocketClient.callMethod(
    method: String,
    params: List<P>
): T {
    return callMethod(method, serializer(), params, serializer())
}

inline fun <reified T, reified P> DdpWebsocketClient.subscribe(
    name: String, params: List<P>
): Flow<SubscriptionEvent<T>> {
    return subscribe(name, serializer(), params, serializer())
}
