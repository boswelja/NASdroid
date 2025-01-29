package com.nasdroid.api.v2.exception

import java.io.IOException

/**
 * Thrown when an HTTP request returns a non-OK code. See [RedirectResponseException],
 * [ClientRequestException] and [ServerResponseException] for specific subclasses.
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
) : IOException(description, cause) {
    override fun equals(other: Any?): Boolean {
        return if (other is HttpNotOkException) {
            code == other.code &&
                    description == other.description &&
                    cause == other.cause
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + description.hashCode()
        return result
    }
}

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
open class ClientRequestException(
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
 * Thrown when an HTTP request returns 401 Unauthorized.
 */
class ClientUnauthorizedException(
    description: String,
    cause: Throwable? = null
) : ClientRequestException(UNAUTHORIZED_STATUS_CODE, description, cause) {
    companion object {
        private const val UNAUTHORIZED_STATUS_CODE = 401
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
