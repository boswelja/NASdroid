package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme

/**
 * An opinionated dialog used to allow users to configure an app update operation.
 */
@Composable
fun UpdateAppDialog(
    availableVersions: List<String>,
    onConfirm: (version: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    var selectedVersion by rememberSaveable(availableVersions) { mutableStateOf(availableVersions.firstOrNull()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            FilledTonalButton(
                onClick = { onConfirm(checkNotNull(selectedVersion)) },
                enabled = !loading
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !loading) {
                Text("Cancel")
            }
        },
        text = {
            AnimatedVisibility(
                visible = loading,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            Column {
                UpdateConfigurationSelector(
                    availableVersions = availableVersions,
                    selectedVersion = selectedVersion.orEmpty(),
                    onSelectedVersionChange = { selectedVersion = it },
                    enabled = !loading
                )
            }
        },
        title = {
            Text("Update")
        },
        icon = {
            Icon(Icons.Default.Update, contentDescription = null)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UpdateConfigurationSelector(
    availableVersions: List<String>,
    selectedVersion: String,
    onSelectedVersionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    enabled: Boolean = true
) {
    var isVersionSelectExpanded by remember { mutableStateOf(false) }
    val itemPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
    )
    Column(
        modifier = modifier
            .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding()),
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        ExposedDropdownMenuBox(
            expanded = isVersionSelectExpanded,
            onExpandedChange = { isVersionSelectExpanded = !isVersionSelectExpanded },
            modifier = Modifier.padding(itemPadding)
        ) {
            TextField(
                value = selectedVersion,
                onValueChange = onSelectedVersionChange,
                readOnly = true,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled),
                label = { Text("Version") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
            )
            ExposedDropdownMenu(
                expanded = isVersionSelectExpanded,
                onDismissRequest = { isVersionSelectExpanded = false },
            ) {
                availableVersions.forEach { version ->
                    DropdownMenuItem(
                        text = { Text(version) },
                        onClick = {
                            onSelectedVersionChange(version)
                            isVersionSelectExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun UpdateAppDialogPreview() {
    NasDroidTheme {
        UpdateAppDialog(
            availableVersions = listOf(
                "1.0.3",
                "1.0.2"
            ),
            onConfirm = { /* no-op */ },
            onDismiss = { /* no-op */ }
        )
    }
}
