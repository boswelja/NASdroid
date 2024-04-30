package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.DurationGraph.Companion.toDurationGraph
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all system-related graphs. See [invoke] for details.
 */
class GetSystemGraphs(
    private val reportingV2Api: ReportingV2Api,
    private val calculationDispatcher: CoroutineDispatcher,
) {

    /**
     * Retrieves a list of [Graph] that describes all system-related graphs, or a
     * [ReportingGraphError] if something went wrong. The retrieved data represents the last hour of
     * reporting data.
     */
    suspend operator fun invoke(): StrongResult<List<Graph<*>>, ReportingGraphError> =
        withContext(calculationDispatcher) {
            try {
                val reportingData = reportingV2Api.getGraphData(
                    graphs = listOf(
                        RequestedGraph("uptime", null),
                    ),
                    unit = Units.HOUR,
                    page = 1
                )
                val (uptimeGraph) = reportingData

                return@withContext StrongResult.success(listOf(uptimeGraph.toDurationGraph()))
            } catch (_: IllegalArgumentException) {
                return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
            }
        }
}
