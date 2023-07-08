package com.boswelja.truemanager.dashboard.logic.configuration

import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.DashboardEntry

/**
 * Initializes [DashboardConfiguration] for the current server. See [invoke] for details.
 */
class InitializeDashboard(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api
) {

    /**
     * Prepopulates the dashboard configuration for the current server. If there is already a
     * configuration present, nothing will change.
     */
    suspend operator fun invoke() {
        val serverId = systemV2Api.getHostId()
        if (!configuration.hasAnyEntries(serverId)) {
            configuration.insertEntries(
                DashboardEntry.Type.values().mapIndexed { index, type ->
                    DashboardEntry(
                        uid = 0,
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
