package com.nasdroid.reporting.logic.graph

import com.boswelja.capacity.Capacity.Companion.kibibytes
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.graph.CapacityGraph.Companion.toCapacityGraph
import com.nasdroid.reporting.logic.graph.TemperatureGraph.Companion.toTemperatureGraph
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Retrieves the data needed to display all disk-related graphs. See [invoke] for details.
 */
class GetDiskGraphs(
    private val reportingV2Api: ReportingV2Api,
    private val calculationDispatcher: CoroutineDispatcher,
) {

    /**
     * Retrieves a [DiskGraphs] that describes all disk-related graphs, or a [ReportingGraphError]
     * if something went wrong. The retrieved data represents the last hour of reporting data.
     *
     * @param disks A list of disk identifiers whose utilisation graphs should be retrieved.
     */
    suspend operator fun invoke(
        disks: List<String>
    ): StrongResult<DiskGraphs, ReportingGraphError> = withContext(calculationDispatcher) {
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
            val utilisationGraphs = emptyList<CapacityGraph>().toMutableList()
            val temperatureGraphs = emptyList<TemperatureGraph>().toMutableList()
            reportingData.forEach { graph ->
                if (graph.name == "disk") {
                    utilisationGraphs += graph.toCapacityGraph { it.kibibytes }
                } else {
                    temperatureGraphs += graph.toTemperatureGraph()
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
    val diskUtilisations: List<CapacityGraph>,
    val diskTemperatures: List<TemperatureGraph>
)
