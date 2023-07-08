package com.boswelja.truemanager.dashboard.logic.dataloading

import com.boswelja.truemanager.core.api.v2.system.SystemInfo
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Takes data received from the server and maps it to a [DashboardData.SystemInformationData]. See
 * [invoke] for details.
 */
class ExtractSystemInformationData {

    /**
     * Produces a [DashboardData.SystemInformationData] from information retrieved from the server.
     *
     * @param systemInfo The server system information.
     * @param uid A unique identifier for the item that will be returned.
     */
    operator fun invoke(
        systemInfo: SystemInfo,
        uid: String,
    ): DashboardData.SystemInformationData {
        return DashboardData.SystemInformationData(
            uid = uid,
            version = systemInfo.version,
            hostname = systemInfo.hostName,
            lastBootTime = Instant.fromEpochMilliseconds(systemInfo.bootTime)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}
