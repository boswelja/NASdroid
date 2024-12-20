package com.nasdroid.api.websocket.ddp

class MethodCallError(
    val error: String,
    val errorType: String,
    val reason: String? = null,
    override val message: String? = null,
) : Throwable()
