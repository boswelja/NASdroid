package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.core.api.v2.system.SystemInfo
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExtractSystemInformationData {

    operator fun invoke(systemInfo: SystemInfo): DashboardData.SystemInformationData {
        return DashboardData.SystemInformationData(
            version = systemInfo.version,
            hostname = systemInfo.hostName,
            lastBootTime = Instant.fromEpochMilliseconds(systemInfo.bootTime)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }
}