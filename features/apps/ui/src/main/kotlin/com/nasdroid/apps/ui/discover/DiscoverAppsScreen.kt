package com.nasdroid.apps.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.apps.logic.discover.SortMode
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.navigation.BackNavigationScaffold
import org.koin.androidx.compose.koinViewModel

/**
 * A screen that allows the user to find and install new apps on their system.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverAppsScreen(
    onAppClick: (appId: String, appCatalog: String, appTrain: String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DiscoverAppsViewModel = koinViewModel()
) {
    val isFilterLoading by viewModel.isFilterLoading.collectAsState()
    val isAppListLoading by viewModel.isAppListLoading.collectAsState()
    val availableAppGroups by viewModel.availableApps.collectAsState()
    val catalogFiltering by viewModel.catalogFilter.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()

    var isFilterSettingsVisible by rememberSaveable { mutableStateOf(false) }
    BackNavigationScaffold(
        title = { Text("Discover Apps") },
        onNavigateBack = navigateBack,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = isFilterLoading || isAppListLoading,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = ""
        ) { isLoading ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(it),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        FilterChipRow(
                            onFilterSettingsClick = { isFilterSettingsVisible = true },
                            onSortModeChange = viewModel::setSortMode,
                            sortMode = sortMode,
                            onCatalogFilterChange = { catalogName, _ -> viewModel.toggleCatalogFiltered(catalogName) },
                            catalogFilters = catalogFiltering,
                            onRemoveSelectedCategory = viewModel::removeSelectedCategory,
                            selectedCategories = selectedCategories,
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        )
                    }
                    items(availableAppGroups) { appGroup ->
                        Text(
                            text = appGroup.groupTitle,
                            style = MaterialThemeExt.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        LazyHorizontalAppList(
                            onAppClick = {
                                onAppClick(it.id, it.catalogName, it.catalogTrain)
                            },
                            apps = appGroup.apps,
                            cellSize = DpSize(width = 300.dp, height = 120.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

    if (isFilterSettingsVisible) {
        val categories by viewModel.availableCategories.collectAsState()
        ModalBottomSheet(
            onDismissRequest = { isFilterSettingsVisible = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            FilterSettings(
                sortMode = sortMode,
                onSortModeChange = viewModel::setSortMode,
                categories = categories,
                selectedCategories = selectedCategories,
                onCategorySelectedChange = { category, selected ->
                    if (selected) {
                        viewModel.addCategoryToFilter(category)
                    } else {
                        viewModel.removeSelectedCategory(category)
                    }
                },
                catalogs = catalogFiltering,
                onCatalogSelectedChange = { catalog, _ -> viewModel.toggleCatalogFiltered(catalog) },
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

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
    onAppClick: (AvailableApp) -> Unit,
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
                    onClick = { onAppClick(app) },
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
                    onClick = { onAppClick(app) },
                    app = app,
                    modifier = Modifier.size(cellSize)
                )
            }
        }
    }
}
