package com.nasdroid.apps.ui.installed.details

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme

@Composable
fun RollbackAppDialog() {

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
    contentPadding: PaddingValues = PaddingValues()
) {
    var versionSelectExpanded by remember { mutableStateOf(false) }
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
            expanded = versionSelectExpanded,
            onExpandedChange = { versionSelectExpanded = !versionSelectExpanded },
            modifier = Modifier.padding(itemPadding)
        ) {
            TextField(
                value = selectedVersion,
                onValueChange = onSelectedVersionChange,
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text("Version") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
            )
            ExposedDropdownMenu(
                expanded = versionSelectExpanded,
                onDismissRequest = { versionSelectExpanded = false },
            ) {
                availableVersions.forEach { version ->
                    DropdownMenuItem(
                        text = { Text(version) },
                        onClick = {
                            onSelectedVersionChange(version)
                            versionSelectExpanded = false
                        }
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = 48.dp)
                .clickable { onRollbackSnapshotsChange(!rollbackSnapshots) }
                .padding(itemPadding)
        ) {
            Text("Roll back snapshots")
            Checkbox(checked = rollbackSnapshots, onCheckedChange = null)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RollbackConfigurationSelectorPreview() {
    val availableVersions = listOf(
        "1.2.0",
        "1.1.9",
        "1.1.8"
    )
    var selectedVersion by remember { mutableStateOf(availableVersions.first()) }
    var rollbackSnapshots by remember { mutableStateOf(false) }
    NasDroidTheme {
        RollbackConfigurationSelector(
            availableVersions = availableVersions,
            selectedVersion = selectedVersion,
            rollbackSnapshots = rollbackSnapshots,
            onRollbackSnapshotsChange = { rollbackSnapshots = it },
            onSelectedVersionChange = { selectedVersion = it },
            contentPadding = PaddingValues(16.dp)
        )
    }
}