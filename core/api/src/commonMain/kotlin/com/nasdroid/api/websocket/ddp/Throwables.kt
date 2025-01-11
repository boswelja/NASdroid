package com.nasdroid.api.websocket.ddp

/**
 * A DDP websocket server responded with an error after attempting to perform a method call.
 */
data class MethodCallError(
    val error: String,
    val errorType: String,
    val reason: String? = null,
    override val message: String? = null,
) : Throwable()
