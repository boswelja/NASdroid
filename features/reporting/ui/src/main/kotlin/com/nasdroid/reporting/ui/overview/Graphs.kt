package com.nasdroid.reporting.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.reporting.logic.graph.GraphData
import com.nasdroid.temperature.TemperatureUnit
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit

@Composable
fun Graph(
    graph: ReportingGraph,
    modifier: Modifier = Modifier
) {
    ProvideChartStyle(m3ChartStyle()) {
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
        verticalAxisValueFormatter = { value, _ ->
            "%.2f".format(value * 100)
        },
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
    val model = remember(data, dataTransform) {
        val lines = data.dataSlices.first().data.size
        val entries = (0 until lines).map { lineIndex ->
            entriesOf(*data.dataSlices.map { dataTransform(it.data[lineIndex]) }.toTypedArray())
        }
        entryModelOf(*entries.toTypedArray())
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        Text(
            text = data.name,
            style = MaterialThemeExt.typography.titleMedium,
        )
        Chart(
            chart = lineChart(
                lines = currentChartStyle.lineChart.lines.map { it.copy(lineBackgroundShader = null) }
            ),
            model = model,
            startAxis = rememberStartAxis(
                title = verticalLabel,
                titleComponent = textComponent(),
                valueFormatter = verticalAxisValueFormatter
            ),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { value, _ ->
                    data.dataSlices[value.toInt()].timestamp
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .let {
                            "${it.hour}:${it.minute}"
                        }
                },
                itemPlacer = remember { AxisItemPlacer.Horizontal.default(spacing = 2) }
            ),
            legend = horizontalLegend(
                items = data.legend.mapIndexed { index, legend ->
                    legendItem(
                        icon = shapeComponent(
                            color = Color(
                                currentChartStyle
                                    .lineChart
                                    .lines[index % currentChartStyle.lineChart.lines.size]
                                    .lineColor
                            )
                        ),
                        label = textComponent(),
                        labelText = legend
                    )
                },
                iconSize = 8.dp,
                iconPadding = MaterialThemeExt.paddings.tiny,
                spacing = MaterialThemeExt.paddings.medium
            ),
            chartScrollSpec = rememberChartScrollSpec(initialScroll = InitialScroll.End),
            isZoomEnabled = false,
        )
    }
}
