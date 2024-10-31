package com.nasdroid.api.websocket

import android.util.Log
import com.nasdroid.api.websocket.message.ConnectMessage
import com.nasdroid.api.websocket.message.ConnectedMessage
import com.nasdroid.api.websocket.message.DdpConnectionMessage
import com.nasdroid.api.websocket.message.FailedMessage
import com.nasdroid.api.websocket.message.MethodMessage
import com.nasdroid.api.websocket.message.ResultMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.serialization.WebsocketDeserializeException
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ServerConnectionProvider(
    private val managementScope: CoroutineScope,
    private val username: String,
    private val password: String,
    val host: String,
    val port: Int?,
) {
    private val client = HttpClient {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("Ktor", message)
                }
            }
        }

        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    private val _connection = MutableStateFlow<DefaultClientWebSocketSession?>(null)

    val connection: Flow<DefaultClientWebSocketSession> = _connection
        .filterNotNull()

    init {
        _connection.subscriptionCount
            .map { it > 0 } // map count into active/inactive flag
            .distinctUntilChanged() // only react to true<->false changes
            .onEach { isActive ->
                if (isActive) {
                    _connection.value = openConnection()
                } else {
                    _connection.value?.close()
                }
            }
            .onCompletion {
                _connection.value?.close()
            }
            .launchIn(managementScope)
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun openConnection(): DefaultClientWebSocketSession {
        val session = client.webSocketSession(
            method = HttpMethod.Get,
            host = host,
            port = port,
            path = "/websocket"
        )

        session.sendSerialized(ConnectMessage(version = "1", support = listOf("1")))
        // Try to negotiate a session
        withTimeout(5.seconds) {
            while (managementScope.isActive) {
                try {
                    when (val response = session.receiveDeserialized<DdpConnectionMessage>()) {
                        is ConnectMessage -> { /* this is unexpected, just ignore it */ }
                        is ConnectedMessage -> {
                            break
                        }
                        is FailedMessage -> error("Failed to connect to the server: $response")
                    }
                } catch (_: WebsocketDeserializeException) {
                    continue
                }
            }
        }

        // Try to authenticate the session
        val authId = Uuid.random().toString()
        val authRequest = MethodMessage(
            id = authId,
            method = "auth.login",
            params = listOf(JsonPrimitive(username), JsonPrimitive(password))
        )
        session.sendSerialized(authRequest)
        withTimeout(5.seconds) {
            while (managementScope.isActive) {
                try {
                    val response = session.receiveDeserialized<ResultMessage<Boolean>>()
                    if (response.id == authId) {
                        if (response.result == true) {
                            break
                        } else {
                            error("Failed to authenticate: ${response.error}")
                        }
                    }
                } catch (_: WebsocketDeserializeException) {
                    continue
                }
            }
        }

        return session
    }
}

