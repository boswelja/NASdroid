package com.nasdroid.api.websocket.ddp

import com.nasdroid.api.websocket.ddp.message.AddedBeforeMessage
import com.nasdroid.api.websocket.ddp.message.AddedMessage
import com.nasdroid.api.websocket.ddp.message.ChangedMessage
import com.nasdroid.api.websocket.ddp.message.ConnectMessage
import com.nasdroid.api.websocket.ddp.message.ConnectServerMessage
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
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DdpWebsocketClient {
    private val bootstrapLock = Mutex()

    var state: State = State.Disconnected
        private set

    suspend fun connect(url: String, session: String? = null) {
        bootstrapLock.withLock {
            check(state is State.Disconnected) { "Cannot connect when already connected or connecting." }
            val webSocket = WebsocketKtorClient.webSocketSession(urlString = url)
            state = State.Connecting(webSocket)
            webSocket.sendSerialized(ConnectMessage(version = "1", support = listOf("1"), session))
            when (val connectResponse = webSocket.receiveDeserialized<ConnectServerMessage>()) {
                is ConnectedMessage -> {
                    state = State.Connected(webSocket, connectResponse.session)
                }
                is FailedMessage -> {
                    state = State.Disconnected
                    throw IllegalStateException("Failed to connect to DDP server: $connectResponse")
                }
            }
        }
    }

    suspend fun disconnect() {
        bootstrapLock.withLock {
            when (val currentState = state) {
                is State.Connected -> {
                    currentState.webSocketSession.close()
                    state = State.Disconnected
                }
                is State.Connecting -> error("Attempted to disconnect while connecting. This should be impossible!")
                State.Disconnected -> { /* Already disconnected, no-op */ }
            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun <T> callMethod(
        method: String,
        params: List<Any?>
    ): MethodCallResult<T> {
        val currentState = state
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val id = Uuid.random().toString()
        val message = MethodMessage(id, method, params)
        currentState.webSocketSession.sendSerialized(message)
        val result: ResultMessage<T> = currentState.webSocketSession.receiveDeserialized()
        return if (result.result != null) {
            MethodCallResult.Success(result.result)
        } else {
            checkNotNull(result.error) { "The result of the method call was neither success or error!" }
            MethodCallResult.Error(
                error = result.error.error,
                errorType = result.error.errorType,
                reason = result.error.reason,
                message = result.error.message
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun <T> subscribe(name: String, params: List<Any>): Flow<SubscriptionEvent<T>> {
        val currentState = state
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
                currentState.webSocketSession.sendSerialized(sub)
            }
            .onCompletion {
                val unsub = UnsubMessage(id)
                currentState.webSocketSession.sendSerialized(unsub)
                currentState.webSocketSession.receiveDeserialized<NosubMessage>()
            }
    }

    sealed interface State {
        data class Connecting(internal val webSocketSession: DefaultClientWebSocketSession) : State

        data class Connected(internal val webSocketSession: DefaultClientWebSocketSession, internal val sessionId: String) : State

        data object Disconnected : State
    }
}

private val MessageSerializer = Json {
    serializersModule = SerializersModule {
        polymorphic(ServerMessage::class) {
            defaultDeserializer { ServerMessageSerializer }
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
