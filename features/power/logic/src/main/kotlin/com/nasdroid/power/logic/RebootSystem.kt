package com.nasdroid.power.logic

import com.nasdroid.api.v2.exception.ClientUnauthorizedException
import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.core.strongresult.StrongResult

/**
 * Reboots the system. See [invoke] for details.
 */
class RebootSystem(
    private val systemV2Api: SystemV2Api
) {
    /**
     * Reboots the system, and suspends until the task completes. This task can fail, see
     * [RebootError] for detailed failure modes.
     */
    suspend operator fun invoke(): StrongResult<Unit, RebootError> {
        return try {
            systemV2Api.reboot()
            StrongResult.success(Unit)
        } catch (_: ClientUnauthorizedException) {
            StrongResult.failure(RebootError.Unauthorized)
        } catch (e: HttpNotOkException) {
            StrongResult.failure(RebootError.GenericNetworkError(e.code, e.description))
        }
    }
}

/**
 * Encapsulates all possible failure modes for [RebootSystem].
 */
sealed interface RebootError {

    /**
     * The currently authenticated user is not authorized to reboot the system.
     */
    data object Unauthorized : RebootError

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
    ): RebootError
}
