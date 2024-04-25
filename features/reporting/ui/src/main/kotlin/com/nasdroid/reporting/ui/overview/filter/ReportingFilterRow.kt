package com.nasdroid.reporting.ui.overview.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.reporting.ui.overview.FilterOptionState
import com.nasdroid.reporting.ui.overview.ReportingCategory

/**
 * Allows the user to quickly adjust reporting filters, or open a dedicated panel for more
 * fine-grained control.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportingFilterRow(
    category: ReportingCategory,
    onCategoryChange: (ReportingCategory) -> Unit,
    availableDevicesState: FilterOptionState,
    availableMetricsState: FilterOptionState,
    selectedDevices: List<String>,
    onDeviceClick: (String) -> Unit,
    selectedMetrics: List<String>,
    onMetricClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    var isSettingsVisible by rememberSaveable {
        mutableStateOf(false)
    }

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.small)
    ) {
        item {
            FilterSettingsChip(onClick = { isSettingsVisible = true })
        }
        item {
            CategoryChip(category = category, onCategoryChange = onCategoryChange)
        }
        items((availableDevicesState as? FilterOptionState.HasOptions)?.availableOptions.orEmpty()) {
            OptionChip(
                optionName = it,
                selected = selectedDevices.contains(it),
                onClick = { onDeviceClick(it) }
            )
        }
        items((availableMetricsState as? FilterOptionState.HasOptions)?.availableOptions.orEmpty()) {
            OptionChip(
                optionName = it,
                selected = selectedMetrics.contains(it),
                onClick = { onMetricClick(it) }
            )
        }
    }

    if (isSettingsVisible) {
        ModalBottomSheet(onDismissRequest = { isSettingsVisible = false }) {
            ReportingFilterSettings(
                category = category,
                onCategoryChange = onCategoryChange,
                availableDevicesState = availableDevicesState,
                availableMetricsState = availableMetricsState,
                selectedDevices = selectedDevices,
                onDeviceClick = onDeviceClick,
                selectedMetrics = selectedMetrics,
                onMetricClick = onMetricClick,
                modifier = Modifier.padding(MaterialThemeExt.paddings.large)
            )
        }
    }
}

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
 * An opinionated [FilterChip] with a dropdown menu that allows the user to select a [ReportingCategory].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    category: ReportingCategory,
    onCategoryChange: (ReportingCategory) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var pickerExpanded by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = pickerExpanded,
        onExpandedChange = { pickerExpanded = !pickerExpanded },
        modifier = modifier
    ) {
        FilterChip(
            selected = true,
            onClick = { pickerExpanded = true },
            enabled = enabled,
            label = {
                Text(category.label())
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                AnimatedContent(targetState = pickerExpanded, label = "Dropdown Indicator") { expanded ->
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
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = pickerExpanded,
            onDismissRequest = { pickerExpanded = false }
        ) {
            ReportingCategory.entries.forEach { reportingCategory ->
                DropdownMenuItem(
                    text = { Text(reportingCategory.label()) },
                    onClick = {
                        onCategoryChange(reportingCategory)
                        pickerExpanded = false
                    }
                )
            }
        }
    }
}

/**
 * An opinionated [FilterChip] designed to show catalogs as a single toggle-able chip.
 */
@Composable
fun OptionChip(
    optionName: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(optionName) },
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

@Preview(showBackground = true)
@Composable
fun ReportingFilterRowPreview() {
    var category by remember { mutableStateOf(ReportingCategory.CPU) }
    val availableDevicesState by remember {
        mutableStateOf(FilterOptionState.HasOptions(listOf("sda1", "sda2", "sda3")))
    }
    val selectedDevices = remember {
        mutableStateListOf("sda1", "sda2", "sda3")
    }
    val availableMetricsState by remember {
        mutableStateOf(FilterOptionState.NoOptions)
    }
    val selectedMetrics = remember {
        mutableStateListOf<String>()
    }
    ReportingFilterRow(
        category = category,
        onCategoryChange = { category = it },
        availableDevicesState = availableDevicesState,
        availableMetricsState = availableMetricsState,
        selectedDevices = selectedDevices,
        onDeviceClick = { if (selectedDevices.contains(it)) selectedDevices.remove(it) else selectedDevices.add(it) },
        selectedMetrics = selectedMetrics,
        onMetricClick = { if (selectedMetrics.contains(it)) selectedMetrics.remove(it) else selectedMetrics.add(it) },
        contentPadding = PaddingValues(16.dp)
    )
}
