package com.nasdroid.apps.ui.installed.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme

/**
 * An opinionated dialog used to configure options before initiating a rollback of an application
 * installed on the system.
 */
@Composable
fun RollbackAppDialog(
    availableVersions: List<String>,
    onConfirm: (version: String, rollbackSnapshots: Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false
) {
    var selectedVersion by rememberSaveable(availableVersions) { mutableStateOf(availableVersions.firstOrNull()) }
    var isRollbackSnapshots by rememberSaveable { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            FilledTonalButton(
                onClick = { onConfirm(checkNotNull(selectedVersion), isRollbackSnapshots) },
                enabled = !loading
            ) {
                Text("Roll Back")
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
                RollbackConfigurationSelector(
                    availableVersions = availableVersions,
                    selectedVersion = selectedVersion.orEmpty(),
                    rollbackSnapshots = isRollbackSnapshots,
                    onRollbackSnapshotsChange = { isRollbackSnapshots = it },
                    onSelectedVersionChange = { selectedVersion = it },
                    enabled = !loading
                )
            }
        },
        title = {
            Text("Roll Back")
        },
        icon = {
            Icon(Icons.Default.Restore, contentDescription = null)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RollbackConfigurationSelector(
    availableVersions: List<String>,
    selectedVersion: String,
    rollbackSnapshots: Boolean,
    onRollbackSnapshotsChange: (Boolean) -> Unit,
    onSelectedVersionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    enabled: Boolean = true,
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .clickable { onRollbackSnapshotsChange(!rollbackSnapshots) }
                .padding(itemPadding)
        ) {
            Text(
                text = "Roll back snapshots",
                style = MaterialThemeExt.typography.bodyLarge
            )
            Checkbox(checked = rollbackSnapshots, onCheckedChange = null, enabled = enabled)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RollbackAppDialogPreview() {
    val availableVersions = listOf(
        "1.2.0",
        "1.1.9",
        "1.1.8"
    )
    NasDroidTheme {
        RollbackAppDialog(
            availableVersions = availableVersions,
            onConfirm = { _, _ -> /* no-op */ },
            onDismiss = { /* no-op */ },
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RollbackAppDialogLoadingPreview() {
    val availableVersions = emptyList<String>()
    NasDroidTheme {
        RollbackAppDialog(
            availableVersions = availableVersions,
            onConfirm = { _, _ -> /* no-op */ },
            onDismiss = { /* no-op */ },
            modifier = Modifier.padding(12.dp),
            loading = true
        )
    }
}
