package com.nasdroid.api.websocket.jsonrpc

/**
 * A DDP websocket server responded with an error after attempting to perform a method call.
 *
 * @property errorCode The code describing the error type.
 */
data class MethodCallError(
    val errorCode: Int,
    override val message: String? = null,
) : Throwable()

/**
 * Something went wrong while trying to construct the RPC call to the server.
 */
data class SerializeError(override val cause: Throwable): Throwable(cause)

/**
 * Something went wrong while trying to deserialize the response received from the server.
 */
data class DeserializeError(override val cause: Throwable, val resultMessage: String): Throwable(cause)
