package com.nasdroid.reporting.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.navigation.NavigationSuiteScaffold
import com.nasdroid.reporting.logic.graph.Graph
import com.nasdroid.reporting.ui.overview.filter.ReportingFilterRow
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows users to view detailed system metrics over time.
 */
@Composable
fun ReportingOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: ReportingOverviewViewModel = koinViewModel()
) {
    val selectedCategory by viewModel.category.collectAsState()
    val availableDevicesState by viewModel.availableDevices.collectAsState()
    val availableMetricsState by viewModel.availableMetrics.collectAsState()
    val selectedDevices by viewModel.selectedDevices.collectAsState()
    val selectedMetrics by viewModel.selectedMetrics.collectAsState()
    val graphs by viewModel.graphs.collectAsState()

    NavigationSuiteScaffold(
        title = { Text("Reporting") }
    ) {
        ReportingOverviewContent(
            selectedCategory = selectedCategory,
            onCategoryClick = viewModel::setCategory,
            availableDevicesState = availableDevicesState,
            availableMetricsState = availableMetricsState,
            selectedDevices = selectedDevices,
            onDeviceClick = viewModel::toggleDeviceSelected,
            selectedMetrics = selectedMetrics,
            onMetricClick = {},
            graphs = graphs,
            modifier = modifier,
            contentPadding = contentPadding
        )
    }
}

/**
 * The content for the "Reporting Overview" screen. This displays a vertical-scrolling grid of
 * reporting graphs, as well as letting users adjust configurations for displayed data.
 */
@Composable
fun ReportingOverviewContent(
    selectedCategory: ReportingCategory,
    onCategoryClick: (ReportingCategory) -> Unit,
    availableDevicesState: FilterOptionState,
    availableMetricsState: FilterOptionState,
    selectedDevices: List<String>,
    onDeviceClick: (String) -> Unit,
    selectedMetrics: List<String>,
    onMetricClick: (String) -> Unit,
    graphs: List<Graph<*>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 240.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small),
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            ReportingFilterRow(
                category = selectedCategory,
                onCategoryChange = onCategoryClick,
                availableDevicesState = availableDevicesState,
                availableMetricsState = availableMetricsState,
                selectedDevices = selectedDevices,
                onDeviceClick = onDeviceClick,
                selectedMetrics = selectedMetrics,
                onMetricClick = onMetricClick
            )
        }
        items(graphs) { graph ->
            ElevatedCard {
                Graph(
                    graph = graph,
                    modifier = Modifier.padding(
                        horizontal = MaterialThemeExt.paddings.large,
                        vertical = MaterialThemeExt.paddings.medium
                    )
                )
            }
        }
    }
}
