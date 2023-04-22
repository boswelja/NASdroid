package com.boswelja.truemanager.core.api.v2

import java.io.IOException

/**
 * Thrown when an HTTP request returns a non-OK code.
 *
 * @property code The non-200 HTTP status code.
 * @property description A human-readable description of the HTTP status.
 * @param cause The cause (which is saved for later retrieval by the getCause() method). (A null
 * value is permitted, and indicates that the cause is nonexistent or unknown.)
 */
class HttpsNotOkException(
    val code: Int,
    val description: String,
    cause: Throwable? = null
) : IOException(description, cause)
