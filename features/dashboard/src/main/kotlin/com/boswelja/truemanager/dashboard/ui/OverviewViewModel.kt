package com.boswelja.truemanager.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.RequestedGraph
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.dashboard.configuration.DashboardConfiguration
import com.boswelja.truemanager.dashboard.configuration.DashboardEntry
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class OverviewViewModel(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api,
    private val reportingV2Api: ReportingV2Api
) : ViewModel() {

    private val serverId = MutableStateFlow<String?>(null)

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
            SharingStarted.WhileSubscribed(100),
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
                    reportingGraphsToQuery.add(RequestedGraph("cpu", null))
                    reportingGraphsToQuery.add(RequestedGraph("cputemp", null))
                }
                DashboardEntry.Type.MEMORY -> {
                    reportingGraphsToQuery.add(RequestedGraph("memory", null))
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapters = reportingV2Api.getReportingGraphs(null, null, null).first { it.name == "interface" }.identifiers
                    adapters.forEach {
                        reportingGraphsToQuery.add(RequestedGraph("interface", it))
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
                        hostname = systemInformation.hostname,
                        lastBootTime = systemInformation.bootTime.toLocalDateTime(TimeZone.currentSystemDefault())
                    )
                }
                DashboardEntry.Type.CPU -> {
                    val utilisationGraph = graphs.first { it.name == "cpu" }
                    val temperatureGraph = graphs.first { it.name == "cputemp" }
                    val avgUsage = (100 - (utilisationGraph.data.last { !it.contains(null) }.last() ?: 100.0)) / 100.0
                    val temp = (temperatureGraph.data.last { !it.contains(null) } as List<Double>).max().roundToInt()
                    DashboardData.CpuData(
                        name = systemInformation.cpuInfo.model,
                        cores = systemInformation.cpuInfo.physicalCores,
                        threads = systemInformation.cpuInfo.totalCores,
                        avgUsage = avgUsage.toFloat(),
                        tempCelsius = temp
                    )
                }
                DashboardEntry.Type.MEMORY -> {
                    Long.MAX_VALUE
                    val memoryGraph = graphs.first { it.name == "memory" }
                    val memoryData = memoryGraph.data.last { !it.contains(null) } as List<Double>
                    DashboardData.MemoryData(
                        memoryUsed = memoryData[0].toLong().bytes,
                        memoryTotal = systemInformation.physicalMemoryBytes.bytes,
                        isEcc = systemInformation.hasEccMemory
                    )
                }
                DashboardEntry.Type.NETWORK -> {
                    val adapterGraphs = graphs.filter { it.name == "interface" }
                    val adaptersInfo = adapterGraphs
                        .filter { it.data.any { it.any { it != null && it > 0 } } }
                        .map { graph ->
                            val data = graph.data.filter { !it.contains(null) } as List<List<Double>>
                            val start = graph.start.toLocalDateTime(TimeZone.currentSystemDefault())
                            val end = graph.end.toLocalDateTime(TimeZone.currentSystemDefault())
                            DashboardData.NetworkUsageData.AdapterData(
                                name = graph.identifier!!,
                                address = "TODO",
                                receivedBytes = data.map { it[0] },
                                sentBytes = data.map { it[1] },
                                period = start..end
                            )
                        }
                    DashboardData.NetworkUsageData(adaptersInfo)
                }
            }
        }
    }
}
