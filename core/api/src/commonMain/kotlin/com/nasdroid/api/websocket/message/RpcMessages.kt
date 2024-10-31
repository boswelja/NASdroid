package com.nasdroid.api.websocket.message

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Seals concrete definitions for
 * [DDP RPC messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#remote-procedure-calls).
 * These messages are used to make method calls and receive results.
 */
sealed interface DdpRpcMessage : DdpMessage

/**
 * Sent by the client to make a remote procedure call.
 *
 * @property id An arbitrary client-determined identifier for this method call.
 * @property method The name of the method to call.
 * @property params Optional parameters for the method.
 */
@Serializable
data class MethodMessage(
    @Contextual
    @SerialName("id")
    val id: String,
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: List<JsonElement>?
) : DdpRpcMessage, DdpClientMessage {
    @SerialName("msg")
    override val msg: String = "method"
}

/**
 * Received from the server as a response to [MethodMessage].
 *
 * @property id The [MethodMessage.id].
 * @property error An error thrown by the method (or method-not-found).
 * @property result The return value of the method, if any.
 */
@Serializable
data class ResultMessage<T>(
    @Contextual
    @SerialName("id")
    val id: String,
    @SerialName("error")
    val error: Error? = null,
    @SerialName("result")
    val result: T? = null,
) : DdpRpcMessage, DdpServerMessage {
    @SerialName("msg")
    override val msg: String = "result"
}

/**
 * Received from the server as a response to [MethodMessage], indicating that the method
 * calls have updated data on the server.
 *
 * @property methods A list of [MethodMessage.id]s, all of whose writes have been reflected in data
 * messages.
 */
@Serializable
data class UpdatedMessage(
    @SerialName("methods")
    val methods: List<String>
) : DdpRpcMessage, DdpServerMessage {
    @SerialName("msg")
    override val msg: String = "updated"
}
