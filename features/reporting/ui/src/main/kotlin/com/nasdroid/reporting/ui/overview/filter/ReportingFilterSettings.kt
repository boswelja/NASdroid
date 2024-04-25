package com.nasdroid.reporting.ui.overview.filter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.reporting.ui.R
import com.nasdroid.reporting.ui.overview.FilterOptionState
import com.nasdroid.reporting.ui.overview.ReportingCategory

/**
 * Displays all reporting-related filter options for the user to adjust.
 */
@Composable
fun ReportingFilterSettings(
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
    Column(
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CategorySelector(category = category, onCategoryChange = onCategoryChange)
        HorizontalDivider()
        DeviceSelector(
            availableDevicesState = availableDevicesState,
            selectedDevices = selectedDevices,
            onDeviceClick = onDeviceClick
        )
        HorizontalDivider()
        MetricsSelector(
            availableMetricsState = availableMetricsState,
            selectedMetrics = selectedMetrics,
            onMetricClick = onMetricClick
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CategorySelector(
    category: ReportingCategory,
    onCategoryChange: (ReportingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterCategory(
        label = stringResource(R.string.filter_category),
        icon = Icons.Default.Category,
        modifier = modifier
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ReportingCategory.entries.forEach {
                FilterChip(
                    selected = it == category,
                    onClick = { onCategoryChange(it) },
                    label = it.label()
                )
            }
        }
    }
}

@Composable
internal fun MetricsSelector(
    availableMetricsState: FilterOptionState,
    selectedMetrics: List<String>,
    onMetricClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OptionsSelector(
        label = stringResource(R.string.filter_metrics),
        icon = Icons.Default.AutoGraph,
        optionsState = availableMetricsState,
        selectedOptions = selectedMetrics,
        onOptionClick = onMetricClick,
        modifier = modifier
    )
}

@Composable
internal fun DeviceSelector(
    availableDevicesState: FilterOptionState,
    selectedDevices: List<String>,
    onDeviceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OptionsSelector(
        label = stringResource(R.string.filter_devices),
        icon = Icons.Default.Hardware,
        optionsState = availableDevicesState,
        selectedOptions = selectedDevices,
        onOptionClick = onDeviceClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun OptionsSelector(
    label: String,
    icon: ImageVector,
    optionsState: FilterOptionState,
    selectedOptions: List<String>,
    onOptionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterCategory(
        label = label,
        icon = icon,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = optionsState,
            transitionSpec = {
                if (initialState is FilterOptionState.HasOptions && targetState is FilterOptionState.HasOptions) {
                    EnterTransition.None togetherWith ExitTransition.None
                } else {
                    fadeIn() togetherWith fadeOut()
                }
            },
            label = "$label state"
        ) {
            when (it) {
                is FilterOptionState.Error -> {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(R.string.filter_load_failed))
                        },
                        leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                        colors = ListItemDefaults.colors(
                            headlineColor = MaterialThemeExt.colorScheme.error,
                            leadingIconColor = MaterialThemeExt.colorScheme.error,
                            containerColor = Color.Transparent
                        )
                    )
                }
                is FilterOptionState.HasOptions -> {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        it.availableOptions.forEach {
                            FilterChip(
                                selected = selectedOptions.contains(it),
                                onClick = { onOptionClick(it) },
                                label = it
                            )
                        }
                    }
                }
                FilterOptionState.Loading -> { /* TODO */ }
                FilterOptionState.NoOptions -> {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(R.string.filter_no_options))
                        },
                        leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                        colors = ListItemDefaults.colors(
                            headlineColor = MaterialThemeExt.colorScheme.onSurfaceVariant,
                            leadingIconColor = MaterialThemeExt.colorScheme.onSurfaceVariant,
                            containerColor = Color.Transparent
                        )
                    )
                }
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
                style = MaterialThemeExt.typography.titleMedium
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
    androidx.compose.material3.FilterChip(
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

@Preview(showBackground = true)
@Composable
fun ReportingFilterSettingsPreview() {
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
    ReportingFilterSettings(
        category = category,
        onCategoryChange = { category = it },
        availableDevicesState = availableDevicesState,
        availableMetricsState = availableMetricsState,
        selectedDevices = selectedDevices,
        onDeviceClick = { if (selectedDevices.contains(it)) selectedDevices.remove(it) else selectedDevices.add(it) },
        selectedMetrics = selectedMetrics,
        onMetricClick = { if (selectedMetrics.contains(it)) selectedMetrics.remove(it) else selectedMetrics.add(it) },
        modifier = Modifier.padding(16.dp)
    )
}