package com.boswelja.truemanager.apps.ui.installed.logs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.boswelja.truemanager.apps.logic.installed.LogOptions
import com.boswelja.truemanager.apps.logic.installed.SelectedLogOptions

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
        mutableStateOf("")
    }

    val contentValid by remember(selectedPod, selectedContainer, maxLines) {
        derivedStateOf {
            selectedPod.isNotEmpty() && selectedContainer.isNotEmpty() && maxLines.isNotEmpty()
        }
    }

    Column(modifier) {
        AnimatedVisibility(visible = options == null) {
            LinearProgressIndicator()
        }

        PodPicker(
            podOptions = options?.podsAndCharts?.keys.orEmpty().toList(),
            selectedPod = selectedPod,
            onPodSelected = setSelectedPod
        )
        ContainerPicker(
            containerOptions = options?.podsAndCharts?.get(selectedPod).orEmpty(),
            selectedContainer = selectedContainer,
            onContainerSelected = setSelectedContainer
        )
        TextField(
            value = maxLines,
            onValueChange = setMaxLines,
            label = {
                Text("Tail lines")
            },
            isError = maxLines.isEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
            enabled = contentValid
        ) {
            Text("View Logs")
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
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = podPickerExpanded
                )
            },
            label = {
                Text("Pod")
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier
        )
        ExposedDropdownMenu(
            expanded = podPickerExpanded,
            onDismissRequest = {
                podPickerExpanded = false
            }
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
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = containerPickerExpanded
                )
            },
            label = {
                Text("Container")
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier
        )
        ExposedDropdownMenu(
            expanded = containerPickerExpanded,
            onDismissRequest = {
                containerPickerExpanded = false
            }
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
