package com.nasdroid.api.exception

import java.io.IOException

/**
 * Thrown when an HTTP request returns a non-OK code.
 *
 * @property code The non-200 HTTP status code.
 * @property description A human-readable description of the HTTP status.
 * @param cause The cause (which is saved for later retrieval by the getCause() method). (A null
 * value is permitted, and indicates that the cause is nonexistent or unknown.)
 */
open class HttpNotOkException(
    val code: Int,
    val description: String,
    cause: Throwable? = null
) : IOException(description, cause)

/**
 * Thrown when an HTTP request returns a redirect-related response code.
 */
class RedirectResponseException(
    code: Int,
    description: String,
    cause: Throwable? = null
) : HttpNotOkException(code, description, cause) {
    init {
        require(code in VALID_RANGE) { "Invalid redirect response code $code" }
    }

    companion object {
        private val VALID_RANGE = 300..399
    }
}

/**
 * Thrown when an HTTP request returns a client request-related response code.
 */
class ClientRequestException(
    code: Int,
    description: String,
    cause: Throwable? = null
) : HttpNotOkException(code, description, cause) {
    init {
        require(code in VALID_RANGE) { "Invalid client request code $code" }
    }

    companion object {
        private val VALID_RANGE = 400..499
    }
}

/**
 * Thrown when an HTTP request returns a server response-related response code.
 */
class ServerResponseException(
    code: Int,
    description: String,
    cause: Throwable? = null
) : HttpNotOkException(code, description, cause) {
    init {
        require(code in VALID_RANGE) { "Invalid server response code $code" }
    }

    companion object {
        private val VALID_RANGE = 500..599
    }
}
