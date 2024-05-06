package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.CapacityGraph.Companion.toCapacityGraph
import com.nasdroid.reporting.logic.graph.FloatGraph.Companion.toFloatGraph
import com.nasdroid.reporting.logic.graph.PercentageGraph.Companion.toPercentageGraph
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all ZFS-related graphs. See [invoke] for details.
 */
class GetZfsGraphs(
    private val reportingV2Api: ReportingV2Api,
    private val calculationDispatcher: CoroutineDispatcher,
) {

    /**
     * Retrieves a list of [Graph] that describes all ZFS-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     *
     * @param timeframe The frame of time for which the graph data is returned for.
     */
    @Suppress("DestructuringDeclarationWithTooManyEntries") // This is intentional here
    suspend operator fun invoke(
        timeframe: GraphTimeframe = GraphTimeframe.Hour
    ): StrongResult<List<Graph<*>>, ReportingGraphError> = withContext(calculationDispatcher) {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("arcactualrate", null),
                    RequestedGraph("arcrate", null),
                    RequestedGraph("arcsize", null),
                    RequestedGraph("arcresult", "demand_data"),
                    RequestedGraph("arcresult", "prefetch_data")
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
            val (
                cacheHitRateGraph,
                arcHitRateGraph,
                arcSizeGraph,
                arcResultDemandGraph,
                arcResultPrefetchGraph
            ) = reportingData

            return@withContext StrongResult.success(
                listOf(
                    cacheHitRateGraph.toFloatGraph("Events/s"),
                    arcHitRateGraph.toFloatGraph("Events/s"),
                    arcSizeGraph.toCapacityGraph(),
                    arcResultDemandGraph.toPercentageGraph(),
                    arcResultPrefetchGraph.toPercentageGraph()
                )
            )
        } catch (_: IllegalArgumentException) {
            return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }
}
