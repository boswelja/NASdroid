package com.nasdroid.api.websocket.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DdpMessage {
    @SerialName("msg")
    val msg: String
}

@Serializable
sealed interface DdpClientMessage : DdpMessage

@Serializable
sealed interface DdpServerMessage : DdpMessage