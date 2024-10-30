package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * This file contains concrete definitions for
 * [DDP connection messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#establishing-a-ddp-connection).
 * These messages are used exclusively to negotiate a session with the websocket server.
 */

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
) {
    @SerialName("msg")
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
) {
    @SerialName("msg")
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
) {
    @SerialName("msg")
    val msg: String = "failed"
}
