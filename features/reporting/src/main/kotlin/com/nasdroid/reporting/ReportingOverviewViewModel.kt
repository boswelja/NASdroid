package com.nasdroid.reporting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours

class ReportingOverviewViewModel(
    private val reportingV2Api: ReportingV2Api
) : ViewModel() {

    private val clock = Clock.System

    private val availableGraphsByType = MutableStateFlow<Map<GraphType?, List<Graph>>>(emptyMap())

    private val _selectedType = MutableStateFlow(GraphType.CPU)
    val selectedType: StateFlow<GraphType> = _selectedType

    val displayedGraphs: StateFlow<List<GraphWithData>> =
        combine(availableGraphsByType, selectedType) { first, second ->
            first[second] ?: emptyList()
        }.map { graphsForType ->
            if (graphsForType.isEmpty()) return@map emptyList()
            val requestedGraphs = graphsForType.map { RequestedGraph(it.id, it.identifier) }
            val nowTime = clock.now()
            val graphData = reportingV2Api.getGraphData(requestedGraphs, start = nowTime.minus(1.hours), end = nowTime)
            graphsForType.mapIndexed { index, requestedGraph ->
                val data = graphData[index]
                // Sometimes there's segments of missing data. We need to filter those
                val cleanedData = data.data
                    .filter { slice -> slice.none { it == null } }
                    .transpose()
                GraphWithData(
                    id = requestedGraph.id,
                    title = requestedGraph.title,
                    identifier = requestedGraph.identifier,
                    data = cleanedData,
                    start = Instant.fromEpochMilliseconds(data.start),
                    end = Instant.fromEpochMilliseconds(data.end),
                    legend = data.legend,
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val allGraphs = reportingV2Api.getReportingGraphs(null, null, null)
            val mappedGraphs = allGraphs
                .filter { graph ->
                    // Filter out known invalid data
                    !graph.title.contains(IdentifierMarker) || !graph.identifiers.isNullOrEmpty()
                }
                .flatMap { graph ->
                    if (!graph.identifiers.isNullOrEmpty()) {
                        graph.identifiers!!.map { identifier ->
                            Graph(
                                id = graph.name,
                                title = graph.title.replace(IdentifierMarker, identifier),
                                identifier = identifier,
                            )
                        }
                    } else {
                        listOf(
                            Graph(
                                id = graph.name,
                                title = graph.title,
                                identifier = null,
                            )
                        )
                    }
                }
                .groupBy { KnownGraphTypes[it.id] }
            availableGraphsByType.emit(mappedGraphs)
        }
    }

    fun setSelectedType(selectedType: GraphType) {
        _selectedType.value = selectedType
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        val cols = get(0).size
        val rows = size
        return List(cols) { j ->
            List(rows) { i ->
                get(i)[j]
            }
        }
    }

    companion object {
        private val KnownGraphTypes = mapOf(
            "cpu" to GraphType.CPU,
            "cputemp" to GraphType.CPU,
            "load" to GraphType.CPU,
            "disk" to GraphType.DISK,
            "disktemp" to GraphType.DISK,
            "memory" to GraphType.MEMORY,
            "swap" to GraphType.MEMORY,
            "interface" to GraphType.NETWORK,
            "nfsstat" to GraphType.NFS,
            "nfsstatbytes" to GraphType.NFS,
            "df" to GraphType.PARTITION,
            "processes" to GraphType.SYSTEM,
            "uptime" to GraphType.SYSTEM,
            "arcsize" to GraphType.ZFS,
            "arcratio" to GraphType.ZFS,
            "arcresult" to GraphType.ZFS
        )

        private const val IdentifierMarker = "{identifier}"
    }
}

data class Graph(
    val id: String,
    val title: String,
    val identifier: String?
)

data class GraphWithData(
    val id: String,
    val title: String,
    val identifier: String?,
    val data: List<List<Double?>>,
    val start: Instant,
    val end: Instant,
    val legend: List<String>,
)

enum class GraphType {
    CPU,
    DISK,
    MEMORY,
    NETWORK,
    NFS,
    PARTITION,
    SYSTEM,
    ZFS
}
