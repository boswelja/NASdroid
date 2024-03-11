package com.nasdroid.power.logic

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api

/**
 * Shuts down the system. See [invoke] for details.
 */
class ShutdownSystem(
    private val systemV2Api: SystemV2Api
) {
    /**
     * Shuts down the system, and suspends until the task completes. This task can fail.
     */
    suspend operator fun invoke(): Result<Unit> {
        return try {
            systemV2Api.shutdown()
            Result.success(Unit)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}
