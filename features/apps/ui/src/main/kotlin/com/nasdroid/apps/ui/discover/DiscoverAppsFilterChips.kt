package com.nasdroid.apps.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.discover.SortMode
import com.nasdroid.apps.ui.R
import com.nasdroid.design.MaterialThemeExt

/**
 * An opinionated [AssistChip] used to launch a more detailed filter configuration.
 */
@Composable
fun FilterSettingsChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    AssistChip(
        onClick = onClick,
        label = {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        modifier = modifier,
        enabled = enabled,
    )
}

/**
 * An opinionated [FilterChip] designed to show catalogs as a single toggle-able chip.
 */
@Composable
fun CatalogChip(
    catalogName: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onSelect,
        label = { Text(catalogName) },
        enabled = enabled,
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

/**
 * An opinionated [FilterChip] with a dropdown menu that allows the user to select a [SortMode].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortModeChip(
    sortMode: SortMode,
    onSortModeChange: (SortMode) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var isPickerExpanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = isPickerExpanded,
        onExpandedChange = { isPickerExpanded = !isPickerExpanded }
    ) {
        FilterChip(
            selected = true,
            onClick = { isPickerExpanded = true },
            enabled = enabled,
            label = {
                Text(sortMode.label())
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                AnimatedContent(targetState = isPickerExpanded, label = "Dropdown Indicator") { expanded ->
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled)
                .then(modifier)
        )
        ExposedDropdownMenu(
            expanded = isPickerExpanded,
            onDismissRequest = { isPickerExpanded = false }
        ) {
            SortMode.entries.forEach { sortMode ->
                DropdownMenuItem(
                    text = { Text(sortMode.label()) },
                    onClick = {
                        onSortModeChange(sortMode)
                        isPickerExpanded = false
                    }
                )
            }
        }
    }
}

/**
 * An opinionated [FilterChip] used to show the user has included the specified category in their
 * filter.
 */
@Composable
fun SelectedCategoryChip(
    categoryName: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = true,
        onClick = onRemove,
        label = { Text(categoryName) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        modifier = modifier
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
fun FilterSettingsChipPreview() {
    MaterialThemeExt {
        FilterSettingsChip(onClick = { })
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogChipPreview() {
    MaterialThemeExt {
        var isSelected by remember {
            mutableStateOf(true)
        }
        CatalogChip(
            catalogName = "TrueNAS",
            selected = isSelected,
            onSelect = { isSelected = !isSelected }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SortModeChipPreview() {
    MaterialThemeExt {
        var sortMode by remember {
            mutableStateOf(SortMode.Category)
        }
        SortModeChip(
            sortMode = sortMode,
            onSortModeChange = { sortMode = it }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SelectedCategoryChipPreview() {
    MaterialThemeExt {
        SelectedCategoryChip(
            categoryName = "Monitoring",
            onRemove = { }
        )
    }
}
