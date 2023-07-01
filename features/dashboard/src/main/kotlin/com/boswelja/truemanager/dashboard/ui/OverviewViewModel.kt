package com.boswelja.truemanager.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.RequestedGraph
import com.boswelja.truemanager.core.api.v2.system.SystemInfo
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.dashboard.configuration.DashboardConfiguration
import com.boswelja.truemanager.dashboard.configuration.DashboardEntry
import com.boswelja.truemanager.dashboard.ui.overview.DashboardData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Collects data for and handles events from the Dashboard overview screen.
 */
class OverviewViewModel(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api,
    private val reportingV2Api: ReportingV2Api
) : ViewModel() {

    private val serverId = MutableStateFlow<String?>(null)

    /**
     * A List of [DashboardData]. The list is ordered by the users configured display order. If the
     * value is null, data is still loading.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val dashboardData: StateFlow<List<DashboardData>?> = serverId
        .filterNotNull()
        .flatMapLatest { configuration.getVisibleEntries(it) }
        .flatMapLatest { entries ->
            repeatingFlow(10.seconds) {
                getDataForEntries(entries)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    init {
        viewModelScope.launch {
            val serverId = systemV2Api.getHostId()
            this@OverviewViewModel.serverId.value = serverId
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

    @OptIn(ExperimentalTime::class)
    private fun <T> repeatingFlow(interval: Duration, producer: suspend () -> T): Flow<T> = flow {
        while (coroutineContext.isActive) {
            val callTime = measureTime {
                emit(producer())
            }
            delay((interval - callTime).coerceAtLeast(Duration.ZERO))
        }
    }

    private suspend fun getDataForEntries(entries: List<DashboardEntry>): List<DashboardData> {
        // Build a list of graphs to query
        val reportingGraphsToQuery = mutableListOf<RequestedGraph>()
        entries.forEach { entry ->
            when (entry.type) {
                DashboardEntry.Type.SYSTEM_INFORMATION -> { /* no-op. We add this data later */ }
                DashboardEntry.Type.CPU -> {
                    reportingGraphsToQuery.add(RequestedGraph(CPU_GRAPH_NAME, null))
                    reportingGraphsToQuery.add(RequestedGraph(CPU_TEMP_GRAPH_NAME, null))
                }
                DashboardEntry.Type.MEMORY -> {
                    reportingGraphsToQuery.add(RequestedGraph(MEMORY_GRAPH_NAME, null))
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapters = reportingV2Api.getReportingGraphs(
                        limit = null,
                        offset = null,
                        sort = null
                    ).first { it.name == INTERFACE_GRAPH_NAME }.identifiers
                    adapters!!.forEach {
                        reportingGraphsToQuery.add(RequestedGraph(INTERFACE_GRAPH_NAME, it))
                    }
                }
            }
        }
        val graphs = reportingV2Api.getGraphData(reportingGraphsToQuery)

        val systemInformation = systemV2Api.getSystemInfo()
        return entries.map { entry ->
            when (entry.type) {
                DashboardEntry.Type.SYSTEM_INFORMATION -> {
                    DashboardData.SystemInformationData(
                        version = systemInformation.version,
                        hostname = systemInformation.hostName,
                        lastBootTime = systemInformation.bootTime.toLocalDateTime(TimeZone.currentSystemDefault())
                    )
                }
                DashboardEntry.Type.CPU -> {
                    val utilisationGraph = graphs.first { it.name == CPU_GRAPH_NAME }
                    val temperatureGraph = graphs.first { it.name == CPU_TEMP_GRAPH_NAME }
                    createCpuData(systemInformation, utilisationGraph, temperatureGraph)
                }
                DashboardEntry.Type.MEMORY -> {
                    val memoryGraph = graphs.first { it.name == MEMORY_GRAPH_NAME }
                    createMemoryData(systemInformation, memoryGraph)
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapterGraphs = graphs.filter { it.name == INTERFACE_GRAPH_NAME }
                    createNetworkUsageData(adapterGraphs)
                }
            }
        }
    }

    private fun createNetworkUsageData(
        adapterGraphs: List<ReportingGraphData>
    ): DashboardData.NetworkUsageData {
        val adaptersInfo = adapterGraphs
            .filter { graph -> graph.data.any { line -> line.any { point -> point != null && point > 0 } } }
            .map { graph ->
                val data = graph.data.filter { !it.contains(null) } as List<List<Double>>
                val start = Instant.fromEpochMilliseconds(graph.start).toLocalDateTime(TimeZone.currentSystemDefault())
                val end = Instant.fromEpochMilliseconds(graph.end).toLocalDateTime(TimeZone.currentSystemDefault())
                DashboardData.NetworkUsageData.AdapterData(
                    name = graph.identifier!!,
                    address = "TODO",
                    receivedBytes = data.map { it[0] },
                    sentBytes = data.map { it[1] },
                    period = start..end
                )
            }
        return DashboardData.NetworkUsageData(adaptersInfo)
    }

    private fun createMemoryData(
        systemInformation: SystemInfo,
        memoryGraph: ReportingGraphData
    ): DashboardData.MemoryData {
        val memoryData = memoryGraph.data.last { !it.contains(null) } as List<Double>
        return DashboardData.MemoryData(
            memoryUsed = memoryData[0].toLong().bytes,
            memoryFree = memoryData[1].toLong().bytes,
            isEcc = systemInformation.eccMemory
        )
    }

    private fun createCpuData(
        systemInformation: SystemInfo,
        usageGraph: ReportingGraphData,
        temperatureGraph: ReportingGraphData
    ): DashboardData.CpuData {
        @Suppress("MagicNumber")
        val avgUsage = (100 - usageGraph.data.last { !it.contains(null) }.last()!!) / 100.0
        val temp = (temperatureGraph.data.last { !it.contains(null) } as List<Double>).max().roundToInt()
        return DashboardData.CpuData(
            name = systemInformation.cpuModel,
            cores = systemInformation.physicalCores,
            threads = systemInformation.cores,
            utilisation = avgUsage.toFloat(),
            tempCelsius = temp
        )
    }

    companion object {
        private const val CPU_GRAPH_NAME = "cpu"
        private const val CPU_TEMP_GRAPH_NAME = "cputemp"
        private const val MEMORY_GRAPH_NAME = "memory"
        private const val INTERFACE_GRAPH_NAME = "interface"
    }
}
