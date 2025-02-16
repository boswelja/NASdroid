package com.nasdroid.dashboard.logic.configuration

import com.nasdroid.data.configuration.DashboardConfiguration
import com.nasdroid.data.configuration.DashboardEntry
import com.nasdroid.api.websocket.system.SystemApi

/**
 * Save the order of the given list of dashboard data. See [invoke] for details.
 */
class SaveDashboardOrder(
    private val configuration: DashboardConfiguration,
    private val systemApi: SystemApi
) {

    /**
     * Saves the order of the list of [DashboardItem]s to the database configuration store.
     */
    suspend operator fun invoke(data: List<DashboardItem>) {
        val hostId = systemApi.hostId()
        val entries = data.mapIndexed { index, dashboardData ->
            DashboardEntry(
                uid = dashboardData.id,
                type = when (dashboardData.type) {
                    DashboardItem.Type.SystemInformation -> DashboardEntry.Type.SYSTEM_INFORMATION
                    DashboardItem.Type.Cpu -> DashboardEntry.Type.CPU
                    DashboardItem.Type.Memory -> DashboardEntry.Type.MEMORY
                    DashboardItem.Type.Network -> DashboardEntry.Type.NETWORK
                },
                serverId = hostId,
                isVisible = true,
                priority = index
            )
        }
        configuration.update(entries)
    }
}
