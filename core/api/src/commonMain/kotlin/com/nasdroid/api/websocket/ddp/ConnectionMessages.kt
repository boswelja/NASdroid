package com.nasdroid.api.websocket.ddp

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Seals concrete definitions for
 * [DDP connection messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#establishing-a-ddp-connection).
 * These messages are sent by the client exclusively to negotiate a session with the websocket server.
 */
@Serializable
sealed interface ConnectClientMessage : ClientMessage

/**
 * Seals concrete definitions for
 * [DDP connection responses](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#establishing-a-ddp-connection).
 * These messages are received from the server exclusively to negotiate a session with the websocket server.
 */
@Serializable
sealed interface ConnectServerMessage : ServerMessage

/**
 * Sent by the client to request a new connection to the websocket server, or reconnects to an
 * existing session.
 *
 * @property version The proposed protocol version.
 * @property support Protocol versions supported by the client, in order of preference.
 * @property session The current session ID (if reconnecting). This is only needed if reconnecting
 * to an existing session.
 */
@Serializable
data class ConnectMessage(
    @SerialName("version")
    val version: String,
    @SerialName("support")
    val support: List<String>,
    @SerialName("session")
    val session: String? = null,
) : ConnectClientMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "connect"
}

/**
 * Received from the server as a result for [ConnectMessage], indicating the connection was
 * successful.
 *
 * @property session An identifier for the DDP session.
 */
@Serializable
data class ConnectedMessage(
    @SerialName("session")
    val session: String,
) : ConnectServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "connected"
}

/**
 * Received from the server as a result for [ConnectMessage], indicating that the connection failed.
 *
 * @property version A suggested protocol version to connect with
 */
@Serializable
data class FailedMessage(
    @SerialName("version")
    val version: String,
) : ConnectServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "failed"
}

object ConnectServerMessageSerializer : JsonContentPolymorphicSerializer<ConnectServerMessage>(ConnectServerMessage::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ConnectServerMessage> {
        return when (element.jsonObject["msg"]?.jsonPrimitive?.content) {
            "connected" -> ConnectedMessage.serializer()
            "failed" -> FailedMessage.serializer()
            else -> error("Unknown message type for ConnectServerMessage: $element")
        }
    }
}
