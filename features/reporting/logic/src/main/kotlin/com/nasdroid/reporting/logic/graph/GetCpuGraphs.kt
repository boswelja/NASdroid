package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.datetime.Instant

/**
 * Retrieves the data needed to display all CPU-related graphs. See [invoke] for details.
 */
class GetCpuGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a [CpuGraphs] that describes all CPU-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     */
    suspend operator fun invoke(): StrongResult<CpuGraphs, ReportingGraphError> {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("cpu", null),
                    RequestedGraph("cputemp", null),
                    RequestedGraph("load", null)
                ),
                unit = Units.HOUR,
                page = 1
            )
            val (cpuGraph, cpuTempGraph, loadGraph) = reportingData

            val result = CpuGraphs(
                cpuUsageGraph = cpuGraph.toGraphData(),
                cpuTempGraph = cpuTempGraph.toGraphData(),
                systemLoadGraph = loadGraph.toGraphData(),
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
 * Holds the state of all CPU-related data.
 *
 * @property cpuUsageGraph Holds all data about CPU utilisation, designed to be shown as a graph.
 * @property cpuTempGraph Holds all data about CPU core temperature, designed to be shown as a graph.
 * @property systemLoadGraph Holds all data about system utilisation, designed to be shown as a graph.
 */
data class CpuGraphs(
    val cpuUsageGraph: GraphData,
    val cpuTempGraph: GraphData,
    val systemLoadGraph: GraphData,
)
