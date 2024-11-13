package com.nasdroid.api.websocket.ddp

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Seals concrete definitions for
 * [DDP RPC messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#remote-procedure-calls).
 * These messages are sent from the client to make method calls.
 */
@Serializable
sealed interface RpcClientMessage : ClientMessage

/**
 * Seals concrete definitions for
 * [DDP RPC messages](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#remote-procedure-calls).
 * These messages are received from the server for method call results.
 */
@Serializable
sealed interface RpcServerMessage : ServerMessage

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
    val params: List<@Contextual Any>?
) : RpcClientMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "method"
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
) : RpcServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "result"
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
) : RpcServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "updated"
}
