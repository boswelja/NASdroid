package com.nasdroid.reporting.ui.overview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Graph(
    graph: ReportingGraph,
    modifier: Modifier = Modifier
) {
    when (graph) {
        is ReportingGraph.BitrateGraph -> TODO()
        is ReportingGraph.CapacityGraph -> TODO()
        is ReportingGraph.CapacityPerSecondGraph -> TODO()
        is ReportingGraph.DurationGraph -> TODO()
        is ReportingGraph.EventsPerSecondGraph -> TODO()
        is ReportingGraph.PercentageGraph -> TODO()
        is ReportingGraph.ProcessesGraph -> TODO()
        is ReportingGraph.TemperatureGraph -> TODO()
    }
}

@Composable
fun BitrateGraph(
    graph: ReportingGraph.BitrateGraph,
    modifier: Modifier = Modifier
) {

}
