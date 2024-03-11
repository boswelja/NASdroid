package com.nasdroid.power.logic

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api

class RebootSystem(
    private val systemV2Api: SystemV2Api
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            systemV2Api.reboot()
            Result.success(Unit)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}
