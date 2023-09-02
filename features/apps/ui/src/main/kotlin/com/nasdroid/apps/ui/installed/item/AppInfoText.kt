package com.nasdroid.apps.ui.installed.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.apps.logic.installed.InstalledApplication

@Composable
internal fun AppInfoText(
    installedApplication: InstalledApplication,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row {
            Text(
                text = installedApplication.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            AppStateChip(state = installedApplication.state)
        }
        Text(
            text = installedApplication.version,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = if (installedApplication.updateAvailable) {
                "Update available"
            } else {
                "Up to date"
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
