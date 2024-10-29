package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PingMessage(
    @SerialName("id")
    val id: String? = null
) {
    @SerialName("msg")
    val message: String = "ping"
}

@Serializable
data class PongMessage(
    @SerialName("id")
    val id: String? = null
) {
    @SerialName("msg")
    val message: String = "pong"
}
