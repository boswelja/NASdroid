package com.nasdroid.api.websocket.message

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface DdpMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String
}

@Serializable
sealed interface DdpClientMessage : DdpMessage

@Serializable
sealed interface DdpServerMessage : DdpMessage