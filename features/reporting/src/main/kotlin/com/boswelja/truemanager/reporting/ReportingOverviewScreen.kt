package com.boswelja.truemanager.reporting

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.endAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReportingOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: ReportingOverviewViewModel = koinViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val graphs by viewModel.graphs.collectAsState()

    AnimatedContent(targetState = isLoading, label = "Loading crossfade") { loading ->
        if (loading) {
            CircularProgressIndicator()
        } else {
            ProvideChartStyle(m3ChartStyle()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1), // TODO Adaptive sizing
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = contentPadding,
                    modifier = modifier
                ) {
                    items(
                        items = graphs,
                        key = { graph -> graph.title }
                    ) { graph ->
                        GraphCard(graph = graph)
                    }
                }
            }
        }
    }
}

@Composable
fun GraphCard(
    graph: GraphWithData,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = graph.title,
                style = MaterialTheme.typography.titleMedium
            )
            Chart(
                chart = lineChart(),
                model = graph.entryModel,
                endAxis = endAxis(title = graph.verticalAxisLabel, titleComponent = textComponent()),
                bottomAxis = bottomAxis(),
                legend = verticalLegend(
                    items = graph.legend.map {
                        verticalLegendItem(
                            icon = shapeComponent(Shapes.pillShape),
                            label = textComponent(),
                            labelText = it.key
                        )
                    },
                    iconSize = 8.dp,
                    iconPadding = 4.dp
                ),
            )
        }
    }
}

@Preview
@Composable
fun GraphCardPreview() {
    MaterialTheme {
        ProvideChartStyle(m3ChartStyle()) {
            GraphCard(
                graph = GraphWithData(
                    title = "Graph",
                    entryModel = entryModelOf(entriesOf(0, 1, 2, 1), entriesOf(1, 2, 1, 3)),
                    verticalAxisLabel = "Label",
                    legend = mapOf(
                        "Stat 1" to android.graphics.Color.GREEN,
                        "Stat 2" to android.graphics.Color.RED
                    ),
                ),
            )
        }
    }
}
