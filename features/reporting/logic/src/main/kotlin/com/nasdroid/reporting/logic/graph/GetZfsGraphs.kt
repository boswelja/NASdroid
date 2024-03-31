package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.mebibytes
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.GraphData.Companion.toGraphData

/**
 * Retrieves the data needed to display all ZFS-related graphs. See [invoke] for details.
 */
class GetZfsGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a [ZfsGraphs] that describes all ZFS-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     */
    @Suppress("DestructuringDeclarationWithTooManyEntries") // This is intentional here
    suspend operator fun invoke(): StrongResult<ZfsGraphs, ReportingGraphError> {
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
                actualCacheHitRate = cacheHitRateGraph.toGraphData { slice -> slice.map { it.toFloat() } },
                arcHitRate = arcHitRateGraph.toGraphData { slice -> slice.map { it.toFloat() } },
                arcSize = arcSizeGraph.toGraphData { slice -> slice.map { it.mebibytes } },
                arcDemandResult = arcResultDemandGraph.toGraphData { slice -> slice.map { (it / 100).toFloat() } },
                arcPrefetchResult = arcResultPrefetchGraph.toGraphData { slice -> slice.map { (it / 100).toFloat() } }
            )

            return StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return StrongResult.failure(ReportingGraphError.InvalidGraphData)
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
    val actualCacheHitRate: GraphData<Float>,
    val arcHitRate: GraphData<Float>,
    val arcSize: GraphData<Capacity>,
    val arcDemandResult: GraphData<Float>,
    val arcPrefetchResult: GraphData<Float>
)
