package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.kibibytes
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.GraphData.Companion.toGraphData
import com.nasdroid.temperature.Temperature
import com.nasdroid.temperature.Temperature.Companion.celsius
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all disk-related graphs. See [invoke] for details.
 */
class GetDiskGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a [DiskGraphs] that describes all disk-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     *
     * @param disks A list of disk identifiers whose utilisation graphs should be retrieved.
     */
    suspend operator fun invoke(
        disks: List<String>
    ): StrongResult<DiskGraphs, ReportingGraphError> = withContext(Dispatchers.Default) {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = disks.flatMap {
                    listOf(
                        RequestedGraph("disk", it),
                        RequestedGraph("disktemp", it)
                    )
                },
                unit = Units.HOUR,
                page = 1
            )
            val utilisationGraphs = emptyList<GraphData<Capacity>>().toMutableList()
            val temperatureGraphs = emptyList<GraphData<Temperature>>().toMutableList()
            reportingData.forEach { graph ->
                if (graph.name == "disk") {
                    utilisationGraphs += graph.toGraphData { slice -> slice.map { it.kibibytes } }
                } else {
                    temperatureGraphs += graph.toGraphData { slice -> slice.map { it.celsius } }
                }
            }
            val result = DiskGraphs(
                diskUtilisations = utilisationGraphs,
                diskTemperatures = temperatureGraphs
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
 * @property diskUtilisations Holds utilisation data for all requested disks.
 * @property diskTemperatures Holds temperature data for all requested disks.
 */
data class DiskGraphs(
    val diskUtilisations: List<GraphData<Capacity>>,
    val diskTemperatures: List<GraphData<Temperature>>
)
