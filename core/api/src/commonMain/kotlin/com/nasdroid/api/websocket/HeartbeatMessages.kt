package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * This file contains concrete definitions for
 * [DDP heartbeat messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#heartbeats).
 * These messages are used to ensure the connection is kept alive.
 */

/**
 * A ping message used to keep the connection alive. Expect a [PongMessage] back from the server.
 *
 * @property id Identifier used to correlate with response.
 */
@Serializable
data class PingMessage(
    @SerialName("id")
    val id: String? = null
) {
    @SerialName("msg")
    val message: String = "ping"
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
) {
    @SerialName("msg")
    val message: String = "pong"
}
