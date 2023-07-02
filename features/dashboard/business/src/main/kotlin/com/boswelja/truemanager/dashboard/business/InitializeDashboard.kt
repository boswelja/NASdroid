package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.DashboardEntry

class InitializeDashboard(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api
) {
    suspend operator fun invoke() {
        val serverId = systemV2Api.getHostId()
        if (!configuration.hasAnyEntries(serverId)) {
            configuration.insertEntries(
                DashboardEntry.Type.values().mapIndexed { index, type ->
                    DashboardEntry(
                        type = type,
                        serverId = serverId,
                        isVisible = true,
                        priority = index
                    )
                }
            )
        }
    }
}
