package com.nasdroid.api.websocket.jsonrpc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class RequestMessage<P>(
    @SerialName("id")
    val id: Int,
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: List<P>,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String = "2.0",
)

sealed interface RpcResponse

@Serializable
data class ResponseMessage<T>(
    @SerialName("id")
    val id: Int,
    @SerialName("result")
    val result: T,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
) : RpcResponse

@Serializable
data class ResponseError(
    @SerialName("id")
    val id: Int,
    @SerialName("error")
    val error: Error,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
) : RpcResponse {
    @Serializable
    data class Error(
        val code: Int,
        val message: String,
        val data: JsonObject
    )
}

@Serializable
data class Notification(
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: JsonObject,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
)

class RpcResponseSerializer<T>(
    val responseSerializer: KSerializer<T>
) : JsonContentPolymorphicSerializer<RpcResponse>(RpcResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RpcResponse> {
        if (element !is JsonObject)
            throw SerializationException("Attempted to deserialize $element, but it wasn't a valid object!")

        return if (element.contains("result")) {
            ResponseMessage.serializer(responseSerializer)
        } else {
            ResponseError.serializer()
        }
    }
}
