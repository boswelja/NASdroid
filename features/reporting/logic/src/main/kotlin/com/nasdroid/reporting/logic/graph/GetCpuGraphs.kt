package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.FloatGraph.Companion.toFloatGraph
import com.nasdroid.reporting.logic.graph.PercentageGraph.Companion.toPercentageGraph
import com.nasdroid.reporting.logic.graph.TemperatureGraph.Companion.toTemperatureGraph
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all CPU-related graphs. See [invoke] for details.
 */
class GetCpuGraphs(
    private val reportingV2Api: ReportingV2Api,
    private val calculationDispatcher: CoroutineDispatcher,
) {

    /**
     * Retrieves a list of [Graph] that describes all CPU-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     *
     * @param timeframe The frame of time for which the graph data is returned for.
     */
    suspend operator fun invoke(
        timeframe: GraphTimeframe = GraphTimeframe.Hour
    ): StrongResult<List<Graph<*>>, ReportingGraphError> = withContext(calculationDispatcher) {
        val reportingData = reportingV2Api.getGraphData(
            graphs = listOf(
                RequestedGraph("cpu", null),
                RequestedGraph("cputemp", null),
                RequestedGraph("load", null),
                RequestedGraph("processes", null)
            ),
            unit = when (timeframe) {
                GraphTimeframe.Hour -> Units.HOUR
                GraphTimeframe.Day -> Units.DAY
                GraphTimeframe.Week -> Units.WEEK
                GraphTimeframe.Month -> Units.MONTH
                GraphTimeframe.Year -> Units.YEAR
            },
            page = 1
        )
        val (cpuGraph, cpuTempGraph, loadGraph, processesGraph) = reportingData

        try {
            return@withContext StrongResult.success(
                listOf(
                    cpuGraph.toPercentageGraph(),
                    cpuTempGraph.toTemperatureGraph(),
                    loadGraph.toFloatGraph("Load"),
                    processesGraph.toFloatGraph("Processes")
                )
            )
        } catch (_: IllegalArgumentException) {
            return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }
}
