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
     * Retrieves a [CpuGraphs] that describes all CPU-related graphs, or a [ReportingGraphError] if
     * something went wrong. The retrieved data represents the last hour of reporting data.
     */
    suspend operator fun invoke(): StrongResult<CpuGraphs, ReportingGraphError> = withContext(calculationDispatcher) {
        try {
            val reportingData = reportingV2Api.getGraphData(
                graphs = listOf(
                    RequestedGraph("cpu", null),
                    RequestedGraph("cputemp", null),
                    RequestedGraph("load", null)
                ),
                unit = Units.HOUR,
                page = 1
            )
            val (cpuGraph, cpuTempGraph, loadGraph) = reportingData

            val result = CpuGraphs(
                cpuUsageGraph = cpuGraph.toPercentageGraph(),
                cpuTempGraph = cpuTempGraph.toTemperatureGraph(),
                systemLoadGraph = loadGraph.toFloatGraph("Processes"),
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
 * @property cpuUsageGraph Holds all data about CPU utilisation, designed to be shown as a graph.
 * @property cpuTempGraph Holds all data about CPU core temperature, designed to be shown as a graph.
 * @property systemLoadGraph Holds all data about system utilisation, designed to be shown as a graph.
 */
data class CpuGraphs(
    val cpuUsageGraph: PercentageGraph,
    val cpuTempGraph: TemperatureGraph,
    val systemLoadGraph: FloatGraph,
)
