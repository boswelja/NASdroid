package com.nasdroid.reporting.ui.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.temperature.TemperatureUnit
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import kotlin.time.DurationUnit

@Composable
fun Graph(
    graph: ReportingGraph,
    modifier: Modifier = Modifier
) {
    when (graph) {
        is ReportingGraph.BitrateGraph -> BitrateGraph(graph = graph, modifier = modifier)
        is ReportingGraph.CapacityGraph -> CapacityGraph(graph = graph, modifier = modifier)
        is ReportingGraph.CapacityPerSecondGraph -> CapacityPerSecondGraph(graph = graph, modifier = modifier)
        is ReportingGraph.DurationGraph -> DurationGraph(graph = graph, modifier = modifier)
        is ReportingGraph.EventsPerSecondGraph -> EventsPerSecondGraph(graph = graph, modifier = modifier)
        is ReportingGraph.PercentageGraph -> PercentageGraph(graph = graph, modifier = modifier)
        is ReportingGraph.ProcessesGraph -> ProcessesGraph(graph = graph, modifier = modifier)
        is ReportingGraph.TemperatureGraph -> TemperatureGraph(graph = graph, modifier = modifier)
    }
}

@Composable
fun BitrateGraph(
    graph: ReportingGraph.BitrateGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex].toDouble(CapacityUnit.MEBIBYTE) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun CapacityGraph(
    graph: ReportingGraph.CapacityGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex].toDouble(CapacityUnit.GIGABYTE) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun CapacityPerSecondGraph(
    graph: ReportingGraph.CapacityPerSecondGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex].toDouble(CapacityUnit.MEBIBYTE) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun DurationGraph(
    graph: ReportingGraph.DurationGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex].toDouble(DurationUnit.DAYS) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun EventsPerSecondGraph(
    graph: ReportingGraph.EventsPerSecondGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex] })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun PercentageGraph(
    graph: ReportingGraph.PercentageGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex] })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun ProcessesGraph(
    graph: ReportingGraph.ProcessesGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex] })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}

@Composable
fun TemperatureGraph(
    graph: ReportingGraph.TemperatureGraph,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember(graph) {
        CartesianChartModelProducer.build {
            val lines = graph.data.dataSlices.first().data.size
            for (lineIndex in 0 until lines) {
                lineSeries {
                    series(graph.data.dataSlices.map { it.data[lineIndex].toDouble(TemperatureUnit.CELSIUS) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(rememberLineCartesianLayer()),
        modelProducer = modelProducer,
        modifier = modifier
    )
}
