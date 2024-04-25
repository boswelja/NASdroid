package com.nasdroid.reporting.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.CapacityUnit
import com.boswelja.percentage.PercentageStyle
import com.boswelja.temperature.TemperatureUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.reporting.logic.graph.GraphData
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.compose.common.rememberLegendItem
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit

/**
 * Composable function that displays a variety of graphs based on the type of [ReportingGraph] provided.
 *
 * @param graph Instance of the sealed class [ReportingGraph]. This can be any subtype of
 * [ReportingGraph], such as [ReportingGraph.BitrateGraph], [ReportingGraph.CapacityGraph], etc.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
@Composable
fun Graph(
    graph: ReportingGraph,
    modifier: Modifier = Modifier
) {
    ProvideVicoTheme(theme = rememberM3VicoTheme()) {
        when (graph) {
            is ReportingGraph.BitrateGraph -> BitrateGraph(graph = graph, modifier = modifier)
            is ReportingGraph.CapacityGraph -> CapacityGraph(graph = graph, modifier = modifier)
            is ReportingGraph.CapacityPerSecondGraph -> CapacityPerSecondGraph(
                graph = graph,
                modifier = modifier
            )
            is ReportingGraph.DurationGraph -> DurationGraph(graph = graph, modifier = modifier)
            is ReportingGraph.EventsPerSecondGraph -> EventsPerSecondGraph(graph = graph, modifier = modifier)
            is ReportingGraph.PercentageGraph -> PercentageGraph(graph = graph, modifier = modifier)
            is ReportingGraph.ProcessesGraph -> ProcessesGraph(graph = graph, modifier = modifier)
            is ReportingGraph.TemperatureGraph -> TemperatureGraph(graph = graph, modifier = modifier)
         }
    }
}

/**
 * Composable function that displays a [BitrateGraph]. This graph shows data in terms of Mibibyte/s.
 *
 * @param graph Instance of the subtype [ReportingGraph.BitrateGraph] representing bitrate data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays a [CapacityGraph]. This graph shows data in terms of Gigabytes.
 *
 * @param graph Instance of the subtype [ReportingGraph.CapacityGraph] representing capacity data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays a [CapacityPerSecondGraph]. This graph shows data in terms of
 * Mebibytes per second.
 *
 * @param graph Instance of the subtype [ReportingGraph.CapacityPerSecondGraph] representing
 * capacity per second data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays a [DurationGraph]. This graph shows data in terms of Days.
 *
 * @param graph Instance of the subtype [ReportingGraph.DurationGraph] representing duration data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays an [EventsPerSecondGraph]. This graph shows data in terms of
 * Events per second.
 *
 * @param graph Instance of the subtype [ReportingGraph.EventsPerSecondGraph] representing events
 * per second data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays a [PercentageGraph]. This graph shows data in terms of
 * percentage (%).
 *
 * @param graph Instance of the subtype [ReportingGraph.PercentageGraph] representing percentage data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
@Composable
fun PercentageGraph(
    graph: ReportingGraph.PercentageGraph,
    modifier: Modifier = Modifier
) {
    VicoGraph(
        data = graph.data,
        dataTransform = { it.toDouble(PercentageStyle.FULL) },
        verticalLabel = "%",
        verticalAxisValueFormatter = { value, _, _ ->
            "%.2f".format(value)
        },
        modifier = modifier
    )
}

/**
 * Composable function that displays a [ProcessesGraph]. This graph shows data in terms of Processes.
 *
 * @param graph Instance of the subtype [ReportingGraph.ProcessesGraph] representing processes data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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

/**
 * Composable function that displays a [TemperatureGraph]. This graph shows data in terms of Celsius
 * (°C).
 *
 * @param graph Instance of the subtype [ReportingGraph.TemperatureGraph] representing temperature
 * data.
 * @param modifier Modifier to be applied to this composable. Defaults to [Modifier] if not provided.
 */
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
    verticalAxisValueFormatter: CartesianValueFormatter = CartesianValueFormatter.decimal(),
) {
    val model = remember(data, dataTransform) {
        CartesianChartModelProducer.build {
            lineSeries {
                val lines = data.dataSlices.first().data.size
                (0 until lines).map { lineIndex ->
                    series(data.dataSlices.map { dataTransform(it.data[lineIndex]) })
                }
            }
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        Text(
            text = data.name,
            style = MaterialThemeExt.typography.titleMedium,
        )
        CartesianChartHost(
            modelProducer = model,
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = rememberStartAxis(
                    title = verticalLabel,
                    titleComponent = rememberTextComponent(),
                    valueFormatter = verticalAxisValueFormatter
                ),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { value, _, _ ->
                        data.dataSlices[value.toInt()].timestamp
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .let {
                                "${it.hour}:${it.minute}"
                            }
                    },
                    itemPlacer = remember { AxisItemPlacer.Horizontal.default(spacing = 2) }
                ),
                legend = rememberHorizontalLegend(
                    items = data.legend.mapIndexed { index, legend ->
                        rememberLegendItem(
                            icon = rememberShapeComponent(
                                color = vicoTheme
                                    .lineCartesianLayerColors[index % vicoTheme.lineCartesianLayerColors.size]
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
            scrollState = rememberVicoScrollState(initialScroll = Scroll.Absolute.End),
            runInitialAnimation = false,
        )
    }
}
