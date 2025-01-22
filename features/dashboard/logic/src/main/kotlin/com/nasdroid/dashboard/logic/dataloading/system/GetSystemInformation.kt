package com.nasdroid.dashboard.logic.dataloading.system

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Retrieves basic information about the state of the system. See [invoke] for details.
 */
class GetSystemInformation(
    private val systemV2Api: SystemV2Api
) {

    /**
     * Returns a [Result] that contains either [SystemInformation], or an exception if the request
     * failed.
     */
    suspend operator fun invoke(): Result<SystemInformation> {
        return try {
            val systemInformation = systemV2Api.getSystemInfo()
            Result.success(
                SystemInformation(
                    version = systemInformation.version,
                    hostname = systemInformation.hostName,
                    lastBootTime = systemInformation.bootTime
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                )
            )
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}

/**
 * Contains basic information about a TrueNAS system.
 *
 * @property version The full version the system is running.
 * @property hostname The system hostname. This helps identify the system on the network.
 * @property lastBootTime The time the system was last started. This is used to calculate uptime.
 */
data class SystemInformation(
    val version: String,
    val hostname: String,
    val lastBootTime: LocalDateTime
)
