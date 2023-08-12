package com.boswelja.truemanager.core.api.v2.core

/**
 * Thrown when a job matching the given parameters could not be found.
 */
class JobNotFoundException(message: String? = null, cause: Throwable? = null): Exception(message, cause)
