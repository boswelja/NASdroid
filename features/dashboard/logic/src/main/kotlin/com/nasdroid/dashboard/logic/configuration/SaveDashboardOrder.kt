package com.nasdroid.dashboard.logic.configuration

import com.nasdroid.dashboard.logic.dataloading.DashboardData
import com.nasdroid.data.configuration.DashboardConfiguration
import com.nasdroid.data.configuration.DashboardEntry
import com.nasdroid.api.v2.system.SystemV2Api

/**
 * Save the order of the given list of dashboard data. See [invoke] for details.
 */
class SaveDashboardOrder(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api
) {

    /**
     * Saves the order of the list of [DashboardData] to the database configuration store.
     */
    suspend operator fun invoke(data: List<DashboardData>) {
        val entries = data.mapIndexed { index, dashboardData ->
            DashboardEntry(
                uid = dashboardData.uid,
                type = when (dashboardData) {
                    is DashboardData.CpuData -> DashboardEntry.Type.CPU
                    is DashboardData.MemoryData -> DashboardEntry.Type.MEMORY
                    is DashboardData.NetworkUsageData -> DashboardEntry.Type.NETWORK
                    is DashboardData.SystemInformationData -> DashboardEntry.Type.SYSTEM_INFORMATION
                },
                serverId = systemV2Api.getHostId(),
                isVisible = true,
                priority = index
            )
        }
        configuration.update(entries)
    }
}
