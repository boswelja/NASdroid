package com.nasdroid.dashboard.logic.configuration

import com.nasdroid.data.configuration.DashboardConfiguration
import com.nasdroid.data.configuration.DashboardEntry
import com.nasdroid.api.v2.system.SystemV2Api

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
                DashboardEntry.Type.entries.mapIndexed { index, type ->
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
