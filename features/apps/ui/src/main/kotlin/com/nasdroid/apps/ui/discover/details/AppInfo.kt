package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nasdroid.apps.ui.R
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
                label = stringResource(R.string.discover_details_homepage_label),
                content = it
            )
        }
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_version_label),
            content = version
        )
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_updated_label),
            content = lastUpdatedAt.toString()
        )
        if (sources.isNotEmpty()) {
            AvailableAppListItem(
                label = stringResource(R.string.discover_details_sources_label),
                content = sources.joinToString(separator = ", ")
            )
        }
    }
}
