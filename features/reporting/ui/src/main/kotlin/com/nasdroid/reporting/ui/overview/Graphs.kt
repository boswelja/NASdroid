package com.nasdroid.reporting.ui.overview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.reporting.logic.graph.GraphData
import com.nasdroid.temperature.TemperatureUnit
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.legend.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.legend.rememberLegendItem
import com.patrykandpatrick.vico.compose.m3.theme.rememberM3VicoTheme
import com.patrykandpatrick.vico.compose.theme.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.theme.vicoTheme
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.PercentageFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.lineSeries
import com.patrykandpatrick.vico.core.scroll.Scroll
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit

@Composable
fun Graph(
    graph: ReportingGraph,
    modifier: Modifier = Modifier
) {
    ProvideVicoTheme(theme = rememberM3VicoTheme()) {
        when (graph) {
            is ReportingGraph.BitrateGraph -> BitrateGraph(graph = graph, modifier = modifier)
            is ReportingGraph.CapacityGraph -> CapacityGraph(graph = graph, modifier = modifier)
            is ReportingGraph.CapacityPerSecondGraph -> CapacityPerSecondGraph(graph = graph, modifier = modifier)
            is ReportingGraph.DurationGraph -> DurationGraph(graph = graph, modifier = modifier)
            is ReportingGraph.EventsPerSecondGraph -> EventsPerSecondGraph(graph = graph, modifier = modifier)
            is ReportingGraph.PercentageGraph -> PercentageGraph(graph = graph, modifier = modifier)
            is ReportingGraph.ProcessesGraph -> ProcessesGraph(graph = graph, modifier = modifier)
            is ReportingGraph.TemperatureGraph -> {} //TemperatureGraph(graph = graph, modifier = modifier)
        }
    }
}

@Composable
fun BitrateGraph(
    graph: ReportingGraph.BitrateGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(CapacityUnit.MEBIBYTE) },
        verticalLabel = "Mibibyte/s",
        modifier = modifier
    )
}

@Composable
fun CapacityGraph(
    graph: ReportingGraph.CapacityGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(CapacityUnit.GIGABYTE) },
        verticalLabel = "Gigabytes",
        modifier = modifier
    )
}

@Composable
fun CapacityPerSecondGraph(
    graph: ReportingGraph.CapacityPerSecondGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(CapacityUnit.MEBIBYTE) },
        verticalLabel = "Mebibyte/s",
        modifier = modifier
    )
}

@Composable
fun DurationGraph(
    graph: ReportingGraph.DurationGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(DurationUnit.DAYS) },
        verticalLabel = "Days",
        modifier = modifier
    )
}

@Composable
fun EventsPerSecondGraph(
    graph: ReportingGraph.EventsPerSecondGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it },
        verticalLabel = "Event/s",
        modifier = modifier
    )
}

@Composable
fun PercentageGraph(
    graph: ReportingGraph.PercentageGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it },
        verticalLabel = "%",
        verticalAxisValueFormatter = PercentageFormatAxisValueFormatter(),
        modifier = modifier
    )
}

@Composable
fun ProcessesGraph(
    graph: ReportingGraph.ProcessesGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it },
        verticalLabel = "Processes",
        modifier = modifier
    )
}

@Composable
fun TemperatureGraph(
    graph: ReportingGraph.TemperatureGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(TemperatureUnit.CELSIUS) },
        verticalLabel = "°C",
        modifier = modifier
    )
}

@Composable
internal fun <T> VicoGraph(
    data: GraphData<T>,
    dataTransform: (T) -> Number,
    verticalLabel: String,
    modifier: Modifier = Modifier,
    verticalAxisValueFormatter: AxisValueFormatter<AxisPosition.Vertical.Start> = DecimalFormatAxisValueFormatter(),
) {
    val modelProducer = remember(data, dataTransform) {
        CartesianChartModelProducer.build {
            lineSeries {
                val lines = data.dataSlices.first().data.size
                for (lineIndex in 0 until lines) {
                    series(data.dataSlices.map { dataTransform(it.data[lineIndex]) })
                }
            }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = rememberStartAxis(
                title = verticalLabel,
                titleComponent = rememberTextComponent(),
                valueFormatter = verticalAxisValueFormatter
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, chartValues, verticalAxisPosition ->
                    data.dataSlices[value.toInt()].timestamp
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .let {
                            "${it.hour}:${it.minute}"
                        }
                },
                itemPlacer = remember { AxisItemPlacer.Horizontal.default(spacing = 6) }
            ),
            legend = rememberHorizontalLegend(
                items = data.legend.mapIndexed { index, legend ->
                    rememberLegendItem(
                        icon = rememberShapeComponent(
                            color = vicoTheme
                                .cartesianLayerColors[vicoTheme.cartesianLayerColors.lastIndex % (index + 1)]
                        ),
                        label = rememberTextComponent(),
                        labelText = legend
                    )
                },
                iconSize = 8.dp,
                iconPadding = MaterialThemeExt.paddings.tiny,
                spacing = MaterialThemeExt.paddings.medium
            )
        ),
        modelProducer = modelProducer,
        runInitialAnimation = false,
        horizontalLayout = HorizontalLayout.FullWidth(),
        scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
        modifier = modifier
    )
}
