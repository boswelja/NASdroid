package com.nasdroid.api.websocket

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class MethodMessage(
    @Contextual
    @SerialName("id")
    val id: Uuid,
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: List<String>?
) {
    @SerialName("msg")
    val msg: String = "method"
}

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class ResultMessage<T>(
    @Contextual
    @SerialName("id")
    val id: Uuid,
    @SerialName("error")
    val error: Error? = null,
    @SerialName("result")
    val result: T? = null,
) {
    @SerialName("msg")
    val msg: String = "result"

    @Serializable
    data class Error(
        @SerialName("error")
        val error: String,
        @SerialName("errorType")
        val errorType: String,
        @SerialName("reason")
        val reason: String? = null,
        @SerialName("message")
        val message: String? = null,
    )
}

@Serializable
data class UpdatedMessage(
    @SerialName("methods")
    val methods: List<String>
) {
    @SerialName("msg")
    val msg: String = "updated"
}

@Serializable
data class ErrorMessage(
    @SerialName("reason")
    val reason: String,
    @SerialName("offendingMessage")
    val requestMessage: MethodMessage? = null
) {
    @SerialName("msg")
    val msg: String = "error"
}
