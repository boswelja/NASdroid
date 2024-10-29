package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class ConnectedMessage(
    @SerialName("session")
    val session: String,
) {
    @SerialName("msg")
    val msg: String = "connected"
}

@Serializable
data class FailedMessage(
    @SerialName("version")
    val version: String,
) {
    @SerialName("msg")
    val msg: String = "failed"
}
