package com.nasdroid.api.websocket.message

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Seals concrete definitions for
 * [DDP heartbeat messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#heartbeats).
 * These messages are used to ensure the connection is kept alive.
 */
sealed interface DdpHeartbeatMessage : DdpServerMessage, DdpClientMessage

/**
 * A ping message used to keep the connection alive. Expect a [PongMessage] back from the server.
 *
 * @property id Identifier used to correlate with response.
 */
@Serializable
data class PingMessage(
    @SerialName("id")
    val id: String? = null
) : DdpHeartbeatMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    override val msg: String = "ping"
}

/**
 * A pong message indicating the successful receipt of a [PingMessage].
 *
 * @property id Identifier used to correlate with response.
 */
@Serializable
data class PongMessage(
    @SerialName("id")
    val id: String? = null
) : DdpHeartbeatMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    override val msg: String = "pong"
}
