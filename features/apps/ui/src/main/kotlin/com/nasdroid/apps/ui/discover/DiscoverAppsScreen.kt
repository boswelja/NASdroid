package com.nasdroid.apps.ui.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.SortMode
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows the user to find and install new apps on their system.
 */
@Composable
fun DiscoverAppsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: DiscoverAppsViewModel = koinViewModel()
) {
    val layoutDirection = LocalLayoutDirection.current
    val availableAppGroups by viewModel.availableApps.collectAsState()
    val catalogFiltering by viewModel.catalogFilter.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()
    val cellPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection),
        end = contentPadding.calculateEndPadding(layoutDirection)
    )
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            FilterChipRow(
                onFilterSettingsClick = { /* TODO */ },
                onSortModeChange = viewModel::setSortMode,
                sortMode = sortMode,
                onCatalogFilterChange = { catalogName, _ ->
                    viewModel.toggleCatalogFiltered(catalogName)
                },
                catalogFilters = catalogFiltering,
                onRemoveSelectedCategory = viewModel::removeSelectedCategory,
                selectedCategories = selectedCategories,
                contentPadding = cellPadding
            )
        }
        items(availableAppGroups) { appGroup ->
            Text(
                text = appGroup.groupTitle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(cellPadding)
            )
            Spacer(Modifier.height(4.dp))
            LazyHorizontalAppList(
                apps = appGroup.apps,
                cellSize = DpSize(width = 300.dp, height = 120.dp),
                contentPadding = cellPadding,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Suppress("LongParameterList") // Unfortunately there's not much we can do here.
@Composable
internal fun FilterChipRow(
    onFilterSettingsClick: () -> Unit,
    onSortModeChange: (SortMode) -> Unit,
    sortMode: SortMode,
    onCatalogFilterChange: (String, Boolean) -> Unit,
    catalogFilters: Map<String, Boolean>,
    onRemoveSelectedCategory: (String) -> Unit,
    selectedCategories: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyRow(
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        item {
            FilterSettingsChip(onClick = onFilterSettingsClick)
        }
        item {
            SortModeChip(sortMode = sortMode, onSortModeChange = onSortModeChange)
        }
        items(catalogFilters.toList()) { (catalogName, selected) -> // TODO toList isn't great for performance
            CatalogChip(
                catalogName = catalogName,
                selected = selected,
                onSelect = { onCatalogFilterChange(catalogName, !selected) }
            )
        }
        items(selectedCategories) { category ->
            SelectedCategoryChip(
                categoryName = category,
                onRemove = { onRemoveSelectedCategory(category) }
            )
        }
    }
}

@Composable
internal fun LazyHorizontalAppList(
    apps: List<AvailableApp>,
    cellSize: DpSize,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    cellSpacing: Dp = 8.dp
) {
    if (apps.size > 2) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(cellSpacing),
            verticalArrangement = Arrangement.spacedBy(cellSpacing),
            modifier = Modifier
                .height(cellSize.height * 2 + cellSpacing)
                .then(modifier),
        ) {
            items(apps) { app ->
                AvailableAppCard(
                    app = app,
                    modifier = Modifier.size(cellSize)
                )
            }
        }
    } else {
        LazyRow(
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(cellSpacing),
            modifier = Modifier
                .height(cellSize.height)
                .then(modifier),
        ) {
            items(apps) { app ->
                AvailableAppCard(
                    app = app,
                    modifier = Modifier.size(cellSize)
                )
            }
        }
    }
}
