package com.nasdroid.api.websocket.jsonrpc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Describes an RPC call to be made to the server.
 *
 * @param P The type of [params].
 *
 * @property id An identifier established by the Client.
 * @property method A String containing the name of the method to be invoked. Method names that begin
 * with the word rpc followed by a period character (U+002E or ASCII 46) are reserved for
 * rpc-internal methods and extensions and MUST NOT be used for anything else.
 * @property params A Structured value that holds the parameter values to be used during the invocation
 * of the method.
 * @property jsonRpcVersion A String specifying the version of the JSON-RPC protocol. MUST be exactly
 * "2.0".
 */
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

/**
 * Encapsulates possible responses from a [RequestMessage].
 */
sealed interface RpcResponse

/**
 * Indicates a successful response to [RequestMessage].
 *
 * @param T The type of [result].
 *
 * @property id An identifier established by the Client.
 * @property result The result of the call.
 * @property jsonRpcVersion A String specifying the version of the JSON-RPC protocol. MUST be exactly
 * "2.0".
 */
@Serializable
data class ResponseMessage<T>(
    @SerialName("id")
    val id: Int,
    @SerialName("result")
    val result: T,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
) : RpcResponse

/**
 * Indicates a failed response to [RequestMessage].
 *
 * @property id An identifier established by the Client.
 * @property error [Error].
 * @property jsonRpcVersion A String specifying the version of the JSON-RPC protocol. MUST be exactly
 * "2.0".
 */
@Serializable
data class ResponseError(
    @SerialName("id")
    val id: Int,
    @SerialName("error")
    val error: Error,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
) : RpcResponse {
    /**
     * Describes an error that happened while making an RPC call.
     *
     * @property code A number that indicates the error type that occurred.
     * @property message A short description of the error.
     * @property data An optional element that contains error details.
     */
    @Serializable
    data class Error(
        val code: Int,
        val message: String,
        val data: JsonElement?
    )
}

/**
 * A Notification is a Request object without an "id" member. A Request object that is a
 * Notification signifies the Client's lack of interest in the corresponding Response object, and as
 * such no Response object needs to be returned to the client. The Server MUST NOT reply to a
 * Notification, including those that are within a batch request.
 *
 * Notifications are not confirmable by definition, since they do not have a Response object to be
 * returned. As such, the Client would not be aware of any errors (like e.g. "Invalid params",
 * "Internal error").
 *
 * @property method A String containing the name of the method to be invoked. Method names that begin
 * with the word rpc followed by a period character (U+002E or ASCII 46) are reserved for
 * rpc-internal methods and extensions and MUST NOT be used for anything else.
 * @property params A Structured value that holds the parameter values to be used during the invocation
 * of the method.
 * @property jsonRpcVersion A String specifying the version of the JSON-RPC protocol. MUST be exactly
 * "2.0".
 */
@Serializable
data class Notification(
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: JsonObject,
    @SerialName("jsonrpc")
    val jsonRpcVersion: String,
)

internal class RpcResponseSerializer(
    val responseSerializer: KSerializer<*>
) : JsonContentPolymorphicSerializer<RpcResponse>(RpcResponse::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<RpcResponse> {
        if (element !is JsonObject) {
            throw SerializationException("Attempted to deserialize $element, but it wasn't a valid object!")
        }

        return if (element.contains("result")) {
            ResponseMessage.serializer(responseSerializer)
        } else {
            ResponseError.serializer()
        }
    }
}
