package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant

@Composable
internal fun AppInfo(
    homepage: String?,
    version: String,
    lastUpdatedAt: Instant,
    sources: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        homepage?.let {
            AvailableAppListItem(
                label = "Homepage",
                content = it
            )
        }
        AvailableAppListItem(
            label = "Version",
            content = version
        )
        AvailableAppListItem(
            label = "Last Updated",
            content = lastUpdatedAt.toString()
        )
        if (sources.isNotEmpty()) {
            AvailableAppListItem(
                label = "Sources",
                content = sources.joinToString(separator = ", ")
            )
        }
    }
}
