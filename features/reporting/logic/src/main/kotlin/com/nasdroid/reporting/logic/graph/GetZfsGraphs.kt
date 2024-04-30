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
     * Retrieves a [ZfsGraphs] that describes all ZFS-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     */
    @Suppress("DestructuringDeclarationWithTooManyEntries") // This is intentional here
    suspend operator fun invoke(): StrongResult<ZfsGraphs, ReportingGraphError> = withContext(calculationDispatcher) {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("arcactualrate", null),
                    RequestedGraph("arcrate", null),
                    RequestedGraph("arcsize", null),
                    RequestedGraph("arcresult", "demand_data"),
                    RequestedGraph("arcresult", "prefetch_data")
                ),
                unit = Units.HOUR,
                page = 1
            )
            val (
                cacheHitRateGraph,
                arcHitRateGraph,
                arcSizeGraph,
                arcResultDemandGraph,
                arcResultPrefetchGraph
            ) = reportingData

            val result = ZfsGraphs(
                actualCacheHitRate = cacheHitRateGraph.toFloatGraph("Events/s"),
                arcHitRate = arcHitRateGraph.toFloatGraph("Events/s"),
                arcSize = arcSizeGraph.toCapacityGraph(),
                arcDemandResult = arcResultDemandGraph.toPercentageGraph(),
                arcPrefetchResult = arcResultPrefetchGraph.toPercentageGraph()
            )

            return@withContext StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }
}

/**
 * Holds the state of all CPU-related data.
 *
 * @property actualCacheHitRate Holds the hit rate of the ZFS cache, measured in events per second.
 * @property arcHitRate The hit rate of the ZFS ARC, measured in events per second.
 * @property arcSize The size of the ZFS ARC.
 * @property arcDemandResult TODO
 * @property arcPrefetchResult TODO
 */
data class ZfsGraphs(
    val actualCacheHitRate: FloatGraph,
    val arcHitRate: FloatGraph,
    val arcSize: CapacityGraph,
    val arcDemandResult: PercentageGraph,
    val arcPrefetchResult: PercentageGraph
)
