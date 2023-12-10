package com.nasdroid.apps.ui.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.discover.SortMode
import com.nasdroid.apps.ui.R

private const val CATEGORIES_COLLAPSED_MAX_COUNT = 10

/**
 * A Composable that gives the user more fine-grained control over their filter selection.
 */
@Composable
fun FilterSettings(
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    categories: List<String>,
    selectedCategories: List<String>,
    onCategorySelectedChange: (String, Boolean) -> Unit,
    catalogs: Map<String, Boolean>,
    onCatalogSelectedChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val cellPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
    Column(
        modifier = modifier
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SortModeSelector(
            sortMode = sortMode,
            onSortModeChange = onSortModeChange,
            contentPadding = cellPadding,
        )
        HorizontalDivider(modifier = Modifier.padding(cellPadding))
        CatalogSelector(
            catalogs = catalogs,
            onCatalogSelectedChange = onCatalogSelectedChange,
            modifier = Modifier.padding(cellPadding),
        )
        HorizontalDivider(modifier = Modifier.padding(cellPadding))
        CategorySelector(
            categories = categories,
            selectedCategories = selectedCategories,
            onCategorySelectChange = onCategorySelectedChange,
            modifier = Modifier.padding(cellPadding),
        )
    }
}

@Composable
internal fun SortModeSelector(
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    FilterCategory(
        label = "Sort Mode",
        icon = Icons.AutoMirrored.Default.Sort,
        headerModifier = modifier.padding(contentPadding),
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding
        ) {
            items(SortMode.entries) {
                FilterChip(
                    selected = it == sortMode,
                    onClick = { onSortModeChange(it) },
                    label = it.label(),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CatalogSelector(
    catalogs: Map<String, Boolean>,
    onCatalogSelectedChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterCategory(
        label = "Catalogs",
        icon = Icons.AutoMirrored.Filled.LibraryBooks,
        modifier = modifier,
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            catalogs.forEach { (catalog, selected) ->
                FilterChip(
                    selected = selected,
                    onClick = { onCatalogSelectedChange(catalog, !selected) },
                    label = catalog,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CategorySelector(
    categories: List<String>,
    selectedCategories: List<String>,
    onCategorySelectChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAll by rememberSaveable {
        mutableStateOf(false)
    }
    var searchTerm by rememberSaveable {
        mutableStateOf("")
    }
    val filteredCategories = produceCategoryList(
        searchTerm = searchTerm,
        showAll = showAll,
        categories = categories,
        selectedCategories = selectedCategories
    )

    FilterCategory(
        label = "Categories",
        icon = Icons.Default.Category,
        modifier = modifier,
    ) {
        Column {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                placeholder = {
                    Text("Search categories")
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                trailingIcon = {
                    if (searchTerm.isNotBlank()) {
                        IconButton(onClick = { searchTerm = "" }) {
                            Icon(Icons.Default.Clear, null)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filteredCategories.forEach {
                    val selected = remember(selectedCategories) {
                        selectedCategories.contains(it)
                    }
                    FilterChip(
                        selected = selected,
                        onClick = { onCategorySelectChange(it, !selected) },
                        label = it,
                    )
                }
            }
            TextButton(
                onClick = { showAll = !showAll },
                modifier = Modifier.align(Alignment.End)
            ) {
                if (showAll) {
                    Icon(Icons.Default.ExpandLess, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Show less")
                } else {
                    Icon(Icons.Default.ExpandMore, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Show more")
                }
            }
        }
    }
}

@Composable
private fun produceCategoryList(
    searchTerm: String,
    showAll: Boolean,
    categories: List<String>,
    selectedCategories: List<String>,
): List<String> {
    return remember(categories, selectedCategories, searchTerm, showAll) {
        if (searchTerm.isBlank()) {
            if (showAll) {
                categories
            } else {
                (selectedCategories + categories).toSet().take(CATEGORIES_COLLAPSED_MAX_COUNT)
            }
        } else {
            if (showAll) {
                categories.filter { it.contains(searchTerm, ignoreCase = true) }
            } else {
                (selectedCategories + categories).toSet()
                    .filter { it.contains(searchTerm, ignoreCase = true) }
                    .take(CATEGORIES_COLLAPSED_MAX_COUNT)
            }
        }
    }
}

@Composable
internal fun FilterCategory(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    headerModifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = headerModifier,
        ) {
            Icon(icon, null)
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
internal fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = {
            AnimatedVisibility(
                visible = selected,
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun SortMode.label(): String {
    return when (this) {
        SortMode.Category -> stringResource(R.string.discover_filter_sort_category)
        SortMode.AppName -> stringResource(R.string.discover_filter_sort_name)
        SortMode.CatalogName -> stringResource(R.string.discover_filter_sort_catalog)
        SortMode.UpdatedDate -> stringResource(R.string.discover_filter_sort_updated)
    }
}

@Preview(showBackground = true)
@Composable
fun FilterSettingsPreview() {
    var sortMode by remember { mutableStateOf(SortMode.Category) }
    MaterialTheme {
        FilterSettings(
            sortMode = sortMode,
            onSortModeChange = { sortMode = it },
            categories = listOf(
                "AI",
                "Administration",
                "Backup",
                "CSI",
                "Cloud",
                "Crypto",
                "DIY",
                "DNS",
                "Downloaders",
                "Drivers",
                "GIT",
                "GameServers",
                "HomeAutomation",
                "Media",
                "MediaApp",
                "MediaServer",
                "Network",
                "Networking",
                "Other",
                "Productivity",
                "ProjectManagement",
                "S3",
                "Security",
                "Social",
                "Tools",
                "ai",
                "analytics",
                "archives",
                "auth",
                "authentication",
                "automations",
                "backup",
                "bookmark",
                "bookmarks",
                "browser",
                "cctv",
                "chat",
                "cloud",
                "cms",
                "core",
                "crm",
                "custom",
                "customization",
                "dashboard",
                "data",
                "database",
                "desktops",
                "discord",
                "dns",
                "docker",
                "documentation",
                "download-tools",
                "downloads",
                "duplicates",
                "email",
                "emulator",
                "file-sharing",
                "file-storage",
                "files",
                "finance",
                "financial",
                "forum",
                "games",
                "gaming",
                "generic",
                "git",
                "graywares",
                "home-automation",
                "homeautomation",
                "hosting",
                "incubator",
                "inventory",
                "iot",
                "ldap",
                "learning",
                "life",
                "logs",
                "machine-learning",
                "mail",
                "management",
                "media",
                "metrics",
                "minecraft",
                "monitor",
                "monitoring",
                "mqtt",
                "music",
                "mutlimedia",
                "network",
                "networking",
                "notes",
                "notifications",
                "nvr",
                "office",
                "operators",
                "organizers",
                "productivity",
                "proxy",
                "pxe",
                "radicale",
                "relay",
                "reporting",
                "rss",
                "search",
                "security",
                "smart",
                "smtp",
                "social",
                "speedtest",
                "ssh",
                "statistics",
                "storage",
                "test",
                "tool",
                "tools",
                "torrent",
                "utilities",
                "utility",
                "vehicle",
                "voice",
                "vpn",
                "web",
                "web-server",
                "website",
                "work"
            ),
            selectedCategories = listOf(
                "Networking",
                "Productivity",
                "AI"
            ),
            onCategorySelectedChange = { _, _ -> },
            catalogs = mapOf(
                "TrueNAS" to true,
                "TrueCharts" to true,
            ),
            onCatalogSelectedChange = { _, _ -> },
            modifier = Modifier,
            contentPadding = PaddingValues(16.dp)
        )
    }
}
