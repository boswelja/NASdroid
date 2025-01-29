package com.nasdroid.power.logic

import com.nasdroid.api.v2.exception.ClientUnauthorizedException
import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.core.strongresult.StrongResult

/**
 * Shuts down the system. See [invoke] for details.
 */
class ShutdownSystem(
    private val systemV2Api: SystemV2Api
) {
    /**
     * Shuts down the system, and suspends until the task completes. This task can fail.
     */
    suspend operator fun invoke(): StrongResult<Unit, ShutdownError> {
        return try {
            systemV2Api.shutdown()
            StrongResult.success(Unit)
        } catch (_: ClientUnauthorizedException) {
            StrongResult.failure(ShutdownError.Unauthorized)
        } catch (e: HttpNotOkException) {
            StrongResult.failure(ShutdownError.GenericNetworkError(e.code, e.description))
        }
    }
}

/**
 * Encapsulates all possible failure modes for [ShutdownSystem].
 */
sealed interface ShutdownError {

    /**
     * The currently authenticated user is not authorized to shut down the system.
     */
    data object Unauthorized : ShutdownError

    /**
     * The server returned a status that was not OK, but also not a known failure type.
     *
     * @property httpResponseCode [HTTP response status code](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status).
     * @property httpResponseDescription A description for this failure. This might be raw data sent
     * from the server.
     */
    data class GenericNetworkError(
        val httpResponseCode: Int,
        val httpResponseDescription: String,
    ): ShutdownError
}
