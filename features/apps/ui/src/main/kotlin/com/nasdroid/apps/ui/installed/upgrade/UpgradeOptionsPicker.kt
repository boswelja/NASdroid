package com.nasdroid.apps.ui.installed.upgrade

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.apps.ui.R

/**
 * A Picker that allows users to pick options for an app upgrade.
 */
@Composable
fun UpgradeOptionsPicker(
    targetVersion: String,
    onTargetVersionChanged: (String) -> Unit,
    upgradeMetadata: UpgradeMetadata,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TargetVersionPicker(
            targetVersion = targetVersion,
            onTargetVersionChanged = onTargetVersionChanged,
            availableVersions = upgradeMetadata.availableVersions,
            modifier = Modifier.fillMaxWidth()
        )
        ImagesSection(imagesToBeUpdated = upgradeMetadata.imagesToBeUpdated)
        ChangelogSection(changelog = upgradeMetadata.changelog)
    }
}

@Composable
internal fun ChangelogSection(
    changelog: String,
    modifier: Modifier = Modifier
) {
    var changelogVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 36.dp)
                .clickable { changelogVisible = !changelogVisible }
        ) {
            Text("Changelog")
            Icon(Icons.Default.ExpandMore, contentDescription = null)
        }
        AnimatedVisibility(visible = changelogVisible) {
            if (changelog.isEmpty()) {
                Text("No Changelog")
            } else {
                Text(text = changelog)
            }
        }
    }
}

@Composable
internal fun ImagesSection(
    imagesToBeUpdated: List<String>,
    modifier: Modifier = Modifier
) {
    var imagesVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 36.dp)
                .clickable { imagesVisible = !imagesVisible }
        ) {
            Text("Images to be updated")
            Icon(Icons.Default.ExpandMore, contentDescription = null)
        }
        AnimatedVisibility(visible = imagesVisible) {
            if (imagesToBeUpdated.isEmpty()) {
                Text("There are no images requiring upgrade")
            } else {
                imagesToBeUpdated.forEach {
                    Text(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TargetVersionPicker(
    targetVersion: String,
    onTargetVersionChanged: (String) -> Unit,
    availableVersions: List<String>,
    modifier: Modifier = Modifier
) {
    var versionPickerExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = versionPickerExpanded,
        onExpandedChange = { versionPickerExpanded = !versionPickerExpanded }
    ) {
        TextField(
            readOnly = true,
            value = targetVersion,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = versionPickerExpanded)
            },
            label = { Text(stringResource(R.string.upgrade_options_version)) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = modifier
        )
        ExposedDropdownMenu(
            expanded = versionPickerExpanded,
            onDismissRequest = { versionPickerExpanded = false }
        ) {
            availableVersions.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onTargetVersionChanged(selectionOption)
                        versionPickerExpanded = false
                    },
                    text = {
                        Text(text = selectionOption)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpgradeOptionsPickerPreview() {
    val availableVersions = listOf(
        "1.0",
        "1.1",
        "1.2"
    )
    var targetVersion by remember {
        mutableStateOf(availableVersions.last())
    }
    UpgradeOptionsPicker(
        targetVersion = targetVersion,
        onTargetVersionChanged = { targetVersion = it },
        upgradeMetadata = UpgradeMetadata(
            availableVersions = availableVersions,
            changelog = "",
            imagesToBeUpdated = listOf(),
            appName = "",
            currentVersion = "",
            iconUrl = "",
        ),
        modifier = Modifier.padding(16.dp)
    )
}
