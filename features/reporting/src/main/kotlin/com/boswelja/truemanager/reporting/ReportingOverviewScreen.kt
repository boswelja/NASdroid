package com.boswelja.truemanager.reporting

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReportingOverviewScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportingOverviewViewModel = koinViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val graphs by viewModel.reportingGraphs.collectAsState()

    AnimatedContent(targetState = isLoading, label = "Loading crossfade") { loading ->
        if (loading) {
            CircularProgressIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1), // TODO Adaptive sizing
                modifier = modifier
            ) {
                items(
                    items = graphs,
                    key = { graph -> graph.name }
                ) { graph ->
                    Card {
                        Text(graph.title)
                        Chart(
                            chart = lineChart(),
                            model = entryModelOf(0, 0, 0), // TODO Actually populate data
                            startAxis = startAxis(title = graph.verticalLabel),
                            bottomAxis = bottomAxis(),
                        )
                    }
                }
            }
        }
    }
}
