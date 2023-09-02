package com.nasdroid.dashboard.logic.dataloading

import com.nasdroid.data.configuration.DashboardEntry
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.system.SystemInfo

/**
 * Takes a list of data received from the server and maps it to a list of [DashboardData]. See
 * [invoke] for details.
 */
class ExtractDashboardData(
    private val extractNetworkUsageData: ExtractNetworkUsageData,
    private val extractMemoryUsageData: ExtractMemoryUsageData,
    private val extractCpuUsageData: ExtractCpuUsageData,
    private val extractSystemInformationData: ExtractSystemInformationData,
) {

    /**
     * Produces a list of [DashboardData] for all given [ReportingGraphData]s.
     *
     * @param entries A list of expected dashboard entries.
     * @param graphs A list of reporting graphs from the server.
     * @param systemInfo The system information from the server.
     */
    operator fun invoke(
        entries: List<DashboardEntry>,
        graphs: List<ReportingGraphData>,
        systemInfo: SystemInfo,
    ): List<DashboardData> {
        return entries.map { entry ->
            val uid = entry.uid
            when (entry.type) {
                DashboardEntry.Type.SYSTEM_INFORMATION -> {
                    extractSystemInformationData(uid = uid, systemInfo = systemInfo)
                }
                DashboardEntry.Type.CPU -> {
                    val utilisationGraph = graphs.first { it.name == CPU_GRAPH_NAME }
                    val temperatureGraph = graphs.first { it.name == CPU_TEMP_GRAPH_NAME }
                    extractCpuUsageData(systemInfo, utilisationGraph, temperatureGraph, uid = uid)
                }
                DashboardEntry.Type.MEMORY -> {
                    val memoryGraph = graphs.first { it.name == MEMORY_GRAPH_NAME }
                    extractMemoryUsageData(systemInfo, memoryGraph, uid = uid)
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapterGraphs = graphs.filter { it.name == INTERFACE_GRAPH_NAME }
                    extractNetworkUsageData(adapterGraphs, uid = uid)
                }
            }
        }
    }

    companion object {
        private const val CPU_GRAPH_NAME = "cpu"
        private const val CPU_TEMP_GRAPH_NAME = "cputemp"
        private const val MEMORY_GRAPH_NAME = "memory"
        private const val INTERFACE_GRAPH_NAME = "interface"
    }
}
