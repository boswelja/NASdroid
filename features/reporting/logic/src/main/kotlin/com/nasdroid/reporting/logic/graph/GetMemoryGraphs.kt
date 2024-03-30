package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.datetime.Instant

/**
 * Retrieves the data needed to display all memory-related graphs. See [invoke] for details.
 */
class GetMemoryGraphs(
    private val reportingV2Api: ReportingV2Api,
) {

    /**
     * Retrieves a [MemoryGraphs] that describes all CPU-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     */
    suspend operator fun invoke(): StrongResult<MemoryGraphs, ReportingGraphError> {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("memory", null),
                    RequestedGraph("swap", null),
                ),
                unit = Units.HOUR,
                page = 1
            )
            val (memoryGraph, swapGraph) = reportingData

            val result = MemoryGraphs(
                memoryUtilisation = memoryGraph.toGraphData(),
                swapUtilisation = swapGraph.toGraphData(),
            )

            return StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }

    private fun ReportingGraphData.toGraphData(): GraphData {
        return GraphData(
            dataSlices = data.map {
                val dataNoNulls = it.requireNoNulls()
                GraphData.DataSlice(
                    timestamp = Instant.fromEpochMilliseconds(dataNoNulls.first().toLong()),
                    data = dataNoNulls.drop(1)
                )
            },
            legend = legend.drop(1),
            name = name,
            identifier = identifier,
            start = Instant.fromEpochMilliseconds(start),
            end = Instant.fromEpochMilliseconds(end)
        )
    }
}

/**
 * Holds the state of all memory-related data.
 *
 * @property memoryUtilisation Holds all data about physical memory utilisation, designed to be
 * shown as a graph.
 * @property swapUtilisation Holds all data about SWAP space utilisation, designed to be shown as a
 * graph.
 */
data class MemoryGraphs(
    val memoryUtilisation: GraphData,
    val swapUtilisation: GraphData,
)
