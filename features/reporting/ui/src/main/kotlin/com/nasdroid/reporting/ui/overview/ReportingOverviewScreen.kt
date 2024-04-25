package com.nasdroid.reporting.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import com.nasdroid.design.MaterialThemeExt
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
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small),
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
    ) {
        item(span = { GridItemSpan(this.maxLineSpan) }) {
            ReportingFilterRow(
                category = selectedCategory,
                onCategoryChange = onCategoryClick,
                extraOptions = extraOptions,
                onExtraOptionChange = onExtraOptionClick
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
