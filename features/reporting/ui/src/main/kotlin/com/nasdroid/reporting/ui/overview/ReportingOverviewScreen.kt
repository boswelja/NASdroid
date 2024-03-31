package com.nasdroid.reporting.ui.overview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    val category by viewModel.category.collectAsState()
    val extraOptions by viewModel.extraOptions.collectAsState()
    val graphs by viewModel.graphs.collectAsState()

    ReportingOverviewContent(
        selectedCategory = category,
        onCategoryClick = viewModel::setCategory,
        extraOptions = extraOptions,
        onExtraOptionClick = {},
        graphs = graphs,
        modifier = modifier,
        contentPadding = contentPadding
    )
}

@Composable
fun ReportingOverviewContent(
    selectedCategory: ReportingCategory,
    onCategoryClick: (ReportingCategory) -> Unit,
    extraOptions: Map<String, Boolean>,
    onExtraOptionClick: (String) -> Unit,
    graphs: List<ReportingGraph>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 240.dp),
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            ReportingCategorySelector(
                selectedCategory = selectedCategory,
                onCategoryClick = onCategoryClick
            )
        }
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            ReportingExtraOptionsSelector(
                extraOptions = extraOptions,
                onOptionClick = onExtraOptionClick
            )
        }
        items(graphs) { graph ->
            ElevatedCard {
                Graph(graph)
            }
        }
    }
}
