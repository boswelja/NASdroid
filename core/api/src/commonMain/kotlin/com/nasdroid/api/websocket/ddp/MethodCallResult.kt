package com.nasdroid.api.websocket.ddp

sealed interface MethodCallResult<T> {
    data class Success<T>(val result: T): MethodCallResult<T>

    data class Error<T>(val error: String, val errorType: String, val reason: String? = null, val message: String? = null): MethodCallResult<T>
}
