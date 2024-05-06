package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.BitrateGraph.Companion.toBitrateGraph
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all network-related graphs. See [invoke] for details.
 */
class GetNetworkGraphs(
    private val reportingV2Api: ReportingV2Api,
    private val calculationDispatcher: CoroutineDispatcher,
) {

    /**
     * Retrieves a list of [Graph] that describes all network-related graphs, or a
     * [ReportingGraphError] if something went wrong. The retrieved data represents the last hour of
     * reporting data.
     *
     * @param interfaces A list of network interfaces whose utilisation graphs should be retrieved.
     * @param timeframe The frame of time for which the graph data is returned for.
     */
    suspend operator fun invoke(
        interfaces: List<String>,
        timeframe: GraphTimeframe = GraphTimeframe.Hour
    ): StrongResult<List<Graph<*>>, ReportingGraphError> = withContext(calculationDispatcher) {
        if (interfaces.isEmpty()) {
            return@withContext StrongResult.success(emptyList())
        }
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = interfaces.map {
                    RequestedGraph("interface", it)
                },
                unit = when (timeframe) {
                    GraphTimeframe.Hour -> Units.HOUR
                    GraphTimeframe.Day -> Units.DAY
                    GraphTimeframe.Week -> Units.WEEK
                    GraphTimeframe.Month -> Units.MONTH
                    GraphTimeframe.Year -> Units.YEAR
                },
                page = 1
            )

            return@withContext StrongResult.success(
                reportingData.map { graph ->
                    graph.toBitrateGraph()
                }
            )
        } catch (_: IllegalArgumentException) {
            return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }
}
