package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.datetime.Instant

class GetCpuGraphs(
    private val reportingV2Api: ReportingV2Api
) {

    suspend operator fun invoke(): StrongResult<CpuGraphs, ReportingGraphError> {
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
                cpuUsageGraph = cpuGraph.toGraphData(),
                cpuTempGraph = cpuTempGraph.toGraphData(),
                systemLoadGraph = loadGraph.toGraphData(),
            )

            return StrongResult.success(result)
        } catch (_: IllegalArgumentException) {
            return StrongResult.failure(ReportingGraphError.InvalidGraphData)
        }
    }

    private fun ReportingGraphData.toGraphData(): GraphData {
        return GraphData(
            dataSlices = data.map {
                val dataNoNulls = it.requireNoNulls()
                GraphData.DataSlice(
                    timestamp = Instant.fromEpochMilliseconds(dataNoNulls.first().toLong()),
                    data = dataNoNulls.drop(1)
                )
            },
            legend = legend.drop(1),
            name = name,
            identifier = identifier,
            start = Instant.fromEpochMilliseconds(start),
            end = Instant.fromEpochMilliseconds(end)
        )
    }
}

data class CpuGraphs(
    val cpuUsageGraph: GraphData,
    val cpuTempGraph: GraphData,
    val systemLoadGraph: GraphData,
)
