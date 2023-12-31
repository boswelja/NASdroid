package com.nasdroid.reporting

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReportingOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: ReportingOverviewViewModel = koinViewModel()
) {
    val layoutDirection = LocalLayoutDirection.current
    val selectedType by viewModel.selectedType.collectAsState()
    val graphs by viewModel.displayedGraphs.collectAsState()
    Column(
        modifier = Modifier
            .padding(
                top = contentPadding.calculateTopPadding()
            )
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GraphType.entries.forEach { graphType ->
                FilterChip(
                    selected = graphType == selectedType,
                    onClick = { viewModel.setSelectedType(graphType) },
                    label = { Text(graphType.label()) }
                )
            }
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1), // TODO Adaptive sizing
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection),
                bottom = contentPadding.calculateBottomPadding()
            ),
            modifier = modifier
        ) {
            items(
                items = graphs,
                key = { graph -> graph.id + graph.identifier }
            ) { graph ->
                GraphCard(graph = graph)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GraphCard(
    graph: GraphWithData,
    modifier: Modifier = Modifier,
    style: ChartStyle = m3ChartStyle(),
) {
    val graphModel = remember(graph) {
        entryModelOf(
            *graph.data.map { entriesOf(*it.filterNotNull().toTypedArray()) }.toTypedArray()
        )
    }
//    val lines = remember(style) {
//        style.lineChart.lines.map { lineSpec ->
//            lineSpec.copy(
//                pointConnector = NoCurvePointConnector,
//                lineBackgroundShader = null
//            )
//        }
//    }
    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = graph.title,
                style = MaterialTheme.typography.titleMedium
            )
            ProvideChartStyle(style) {
                Chart(
                    chart = lineChart(),
                    model = graphModel,
                    endAxis = rememberEndAxis(
                        titleComponent = textComponent(),
                    ),
                    chartScrollSpec = rememberChartScrollSpec(false),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                graph.legend.forEachIndexed { index, s ->
                    val color = remember(style.lineChart.lines) {
                        val colorIndex = if (index != 0) {
                            (index % style.lineChart.lines.size)
                        } else {
                            0
                        }
                        Color(style.lineChart.lines[colorIndex].lineColor)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = color,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = s, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Stable
@Composable
private fun GraphType.label(): String {
    val stringRes = remember {
        when (this) {
            GraphType.CPU -> R.string.graph_type_cpu
            GraphType.DISK -> R.string.graph_type_disk
            GraphType.MEMORY -> R.string.graph_type_memory
            GraphType.NETWORK -> R.string.graph_type_network
            GraphType.NFS -> R.string.graph_type_nfs
            GraphType.PARTITION -> R.string.graph_type_partition
            GraphType.SYSTEM -> R.string.graph_type_system
            GraphType.ZFS -> R.string.graph_type_zfs
        }
    }
    return stringResource(stringRes)
}
