package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.datetime.Instant

/**
 * Retrieves the data needed to display all system-related graphs. See [invoke] for details.
 */
class GetSystemGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a [CpuGraphs] that describes all system-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     */
    suspend operator fun invoke(): StrongResult<SystemGraphs, ReportingGraphError> {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("uptime", null),
                ),
                unit = Units.HOUR,
                page = 1
            )
            val (uptimeGraph) = reportingData

            val result = SystemGraphs(
                uptime = uptimeGraph.toGraphData(),
            )

            return StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }

    private fun ReportingGraphData.toGraphData(): GraphData {
        val templatedName = identifier?.let { name.replace("{identifier}", it) } ?: name
        return GraphData(
            dataSlices = data.map {
                val dataNoNulls = it.requireNoNulls()
                GraphData.DataSlice(
                    timestamp = Instant.fromEpochMilliseconds(dataNoNulls.first().toLong()),
                    data = dataNoNulls.drop(1)
                )
            },
            legend = legend.drop(1),
            name = templatedName,
            identifier = identifier,
            start = Instant.fromEpochMilliseconds(start),
            end = Instant.fromEpochMilliseconds(end)
        )
    }
}

/**
 * Holds the state of all CPU-related data.
 *
 * @property uptime Holds all data about system uptime, designed to be shown as a graph.
 */
data class SystemGraphs(
    val uptime: GraphData,
)
