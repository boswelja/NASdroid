package com.boswelja.truemanager.apps.ui.installed.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview

@Composable
internal fun AppInfoText(
    applicationOverview: ApplicationOverview,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row {
            Text(
                text = applicationOverview.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            AppStateChip(state = applicationOverview.state)
        }
        Text(
            text = applicationOverview.version,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = if (applicationOverview.updateAvailable) {
                "Update available"
            } else {
                "Up to date"
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
