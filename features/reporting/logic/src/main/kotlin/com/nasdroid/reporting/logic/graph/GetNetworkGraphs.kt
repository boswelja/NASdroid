package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.kilobytes
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.GraphData.Companion.toGraphData

/**
 * Retrieves the data needed to display all network-related graphs. See [invoke] for details.
 */
class GetNetworkGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    private lateinit var interfaces: List<String>

    /**
     * Retrieves a [NetworkGraphs] that describes all CPU-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     */
    suspend operator fun invoke(): StrongResult<NetworkGraphs, ReportingGraphError> {
        try {
            if (!::interfaces.isInitialized) {
                interfaces = reportingV2Api.getReportingGraphs(null, null, null)
                    .first { it.name == "interface" }
                    .identifiers
                    .orEmpty()
            }
            val reportingData = reportingV2Api.getGraphData(
                graphs = interfaces.map {
                    RequestedGraph("interface", it)
                },
                unit = Units.HOUR,
                page = 1
            )
            val result = NetworkGraphs(
                reportingData.map { graph ->
                    graph.toGraphData { it.map { it.kilobytes } }
                }
            )

            return StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return StrongResult.failure(ReportingGraphError.InvalidGraphData)
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
