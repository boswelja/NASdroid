package com.nasdroid.apps.ui.installed.overview.logs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.logic.installed.LogOptions
import com.nasdroid.apps.logic.installed.SelectedLogOptions
import com.nasdroid.apps.ui.R

/**
 * A form-style picker for log options. This allows the user to construct a [SelectedLogOptions]
 * from a [LogOptions].
 */
@Composable
fun LogOptionsPicker(
    options: LogOptions?,
    onLogOptionsSelected: (SelectedLogOptions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (selectedPod, setSelectedPod) = rememberSaveable(options) {
        mutableStateOf(options?.podsAndCharts?.keys?.firstOrNull().orEmpty())
    }
    val (selectedContainer, setSelectedContainer) = rememberSaveable(selectedPod) {
        mutableStateOf(options?.podsAndCharts?.get(selectedPod)?.firstOrNull().orEmpty())
    }
    val (maxLines, setMaxLines) = rememberSaveable {
        mutableStateOf("500")
    }

    val contentValid by remember(selectedPod, selectedContainer, maxLines) {
        derivedStateOf {
            selectedPod.isNotEmpty() && selectedContainer.isNotEmpty() && maxLines.isNotEmpty()
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(visible = options == null) {
            LinearProgressIndicator()
        }

        PodPicker(
            podOptions = options?.podsAndCharts?.keys.orEmpty().toList(),
            selectedPod = selectedPod,
            onPodSelected = setSelectedPod,
            modifier = Modifier.fillMaxWidth(),
        )
        ContainerPicker(
            containerOptions = options?.podsAndCharts?.get(selectedPod).orEmpty(),
            selectedContainer = selectedContainer,
            onContainerSelected = setSelectedContainer,
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = maxLines,
            onValueChange = setMaxLines,
            label = { Text(stringResource(R.string.log_options_max_lines)) },
            isError = maxLines.isEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                onLogOptionsSelected(
                    SelectedLogOptions(
                        podName = selectedPod,
                        containerName = selectedContainer,
                        maxLines = maxLines.toLong()
                    )
                )
            },
            enabled = contentValid,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.log_options_save))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PodPicker(
    podOptions: List<String>,
    selectedPod: String,
    onPodSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var podPickerExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = podPickerExpanded,
        onExpandedChange = { podPickerExpanded = it }
    ) {
        TextField(
            readOnly = true,
            value = selectedPod,
            onValueChange = onPodSelected,
            enabled = podOptions.isNotEmpty(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = podPickerExpanded)
            },
            label = { Text(stringResource(R.string.log_options_pod)) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = podPickerExpanded,
            onDismissRequest = { podPickerExpanded = false }
        ) {
            podOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onPodSelected(selectionOption)
                        podPickerExpanded = false
                    },
                    text = {
                        Text(text = selectionOption)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContainerPicker(
    containerOptions: List<String>,
    selectedContainer: String,
    onContainerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var containerPickerExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = containerPickerExpanded,
        onExpandedChange = { containerPickerExpanded = it }
    ) {
        TextField(
            readOnly = true,
            value = selectedContainer,
            onValueChange = onContainerSelected,
            enabled = containerOptions.isNotEmpty(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = containerPickerExpanded)
            },
            label = { Text(stringResource(R.string.log_options_container)) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = containerPickerExpanded,
            onDismissRequest = { containerPickerExpanded = false }
        ) {
            containerOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onContainerSelected(selectionOption)
                        containerPickerExpanded = false
                    },
                    text = {
                        Text(text = selectionOption)
                    }
                )
            }
        }
    }
}
