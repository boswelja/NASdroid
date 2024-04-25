package com.nasdroid.reporting.ui.overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

@Composable
fun ReportingFilterRow(
    category: ReportingCategory,
    onCategoryChange: (ReportingCategory) -> Unit,
    extraOptions: Map<String, Boolean>,
    onExtraOptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val extraOptionsList = remember(extraOptions) {
        extraOptions.toList()
    }
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        item {
            CategoryFilterChip(category = category, onCategoryChange = onCategoryChange)
        }
        items(extraOptionsList) { (name, state) ->
            FilterChip(
                selected = state,
                onClick = { onExtraOptionChange(name) },
                label = { Text(name) },
                leadingIcon = {
                    AnimatedVisibility(
                        visible = state,
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
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryFilterChip(
    category: ReportingCategory,
    onCategoryChange: (ReportingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        FilterChip(
            selected = true,
            onClick = { expanded = true },
            label = { Text(category.name) },
            trailingIcon = {
                AnimatedContent(targetState = expanded, label = "Dropdown Indicator") { expanded ->
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
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize(false)
        ) {
            ReportingCategory.entries.forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onCategoryChange(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportingFilterRowPreview() {
    var selectedCategory by remember {
        mutableStateOf(ReportingCategory.CPU)
    }
    val extraOptions = remember {
        mutableStateMapOf(
            "sda1" to true,
            "sda2" to true,
            "sda3" to false
        )
    }

    ReportingFilterRow(
        category = selectedCategory,
        onCategoryChange = { selectedCategory = it },
        extraOptions = extraOptions,
        onExtraOptionChange = { extraOptions[it] = !extraOptions[it]!! }
    )
}