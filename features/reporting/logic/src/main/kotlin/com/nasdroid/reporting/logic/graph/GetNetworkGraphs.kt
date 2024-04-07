package com.nasdroid.reporting.logic.graph

import com.boswelja.capacity.Capacity
import com.boswelja.capacity.Capacity.Companion.kilobytes
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.GraphData.Companion.toGraphData
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
     * Retrieves a [NetworkGraphs] that describes all CPU-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     *
     * @param interfaces A list of network interfaces whose utilisation graphs should be retrieved.
     */
    suspend operator fun invoke(
        interfaces: List<String>
    ): StrongResult<NetworkGraphs, ReportingGraphError> = withContext(calculationDispatcher) {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = interfaces.map {
                    RequestedGraph("interface", it)
                },
                unit = Units.HOUR,
                page = 1
            )
            val result = NetworkGraphs(
                reportingData.map { graph ->
                    graph.toGraphData { slice -> slice.map { it.kilobytes } }
                }
            )

            return@withContext StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return@withContext StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }
}

/**
 * Holds the state of all network-related data.
 *
 * @property networkInterfaces Holds all data about network interface utilisation, designed to be
 * shown as multiple graph.
 */
data class NetworkGraphs(
    val networkInterfaces: List<GraphData<Capacity>>
)
