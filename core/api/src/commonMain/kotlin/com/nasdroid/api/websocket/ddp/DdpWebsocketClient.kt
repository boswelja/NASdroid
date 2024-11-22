package com.nasdroid.api.websocket.ddp

import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DdpWebsocketClient(
    val url: String
) {
    private val bootstrapLock = Mutex()

    var state: State = State.Disconnected
        private set

    suspend fun connect(session: String? = null) {
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
        params: List<Any>
    ): T {
        val currentState = state
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val id = Uuid.random().toString()
        val message = MethodMessage(id, method, params)
        currentState.webSocketSession.sendSerialized(message)
        val result: ResultMessage<T> = currentState.webSocketSession.receiveDeserialized()
        return result.result!! // TODO error handling
    }

    @OptIn(ExperimentalUuidApi::class)
    fun subscribe(name: String, params: List<Any>): Flow<DataManagementServerMessage> {
        val currentState = state
        check(currentState is State.Connected) { "Client must be connected before making any method calls!" }

        val id = Uuid.random().toString()
        return currentState.webSocketSession.incoming.receiveAsFlow()
            .map { value -> currentState.webSocketSession.converter!!.deserialize<DataManagementServerMessage>(value) }
            .onStart {
                val sub = SubMessage(id, name, params)
                currentState.webSocketSession.sendSerialized(sub)
            }
            .onCompletion { cause ->
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
        polymorphic(ServerMessage::class, baseSerializer = ServerMessageSerializer)
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
