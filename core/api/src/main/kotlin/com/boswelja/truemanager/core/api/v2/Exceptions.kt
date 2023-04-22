package com.boswelja.truemanager.core.api.v2

import java.io.IOException

/**
 * Thrown when an HTTP request returns a non-OK code.
 */
class HttpsNotOkException(
    code: Int,
    description: String,
    cause: Throwable? = null
) : IOException(description, cause)
