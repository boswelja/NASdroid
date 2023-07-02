package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import com.boswelja.truemanager.core.api.v2.system.SystemInfo
import com.boswelja.truemanager.data.configuration.DashboardEntry

class ExtractDashboardData(
    private val extractNetworkUsageData: ExtractNetworkUsageData,
    private val extractMemoryUsageData: ExtractMemoryUsageData,
    private val extractCpuUsageData: ExtractCpuUsageData,
    private val extractSystemInformationData: ExtractSystemInformationData,
) {

    operator fun invoke(
        entries: List<DashboardEntry>,
        graphs: List<ReportingGraphData>,
        systemInfo: SystemInfo
    ): List<DashboardData> {
        return entries.map { entry ->
            when (entry.type) {
                DashboardEntry.Type.SYSTEM_INFORMATION -> {
                    extractSystemInformationData(systemInfo)
                }
                DashboardEntry.Type.CPU -> {
                    val utilisationGraph = graphs.first { it.name == CPU_GRAPH_NAME }
                    val temperatureGraph = graphs.first { it.name == CPU_TEMP_GRAPH_NAME }
                    extractCpuUsageData(systemInfo, utilisationGraph, temperatureGraph)
                }
                DashboardEntry.Type.MEMORY -> {
                    val memoryGraph = graphs.first { it.name == MEMORY_GRAPH_NAME }
                    extractMemoryUsageData(systemInfo, memoryGraph)
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapterGraphs = graphs.filter { it.name == INTERFACE_GRAPH_NAME }
                    extractNetworkUsageData(adapterGraphs)
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