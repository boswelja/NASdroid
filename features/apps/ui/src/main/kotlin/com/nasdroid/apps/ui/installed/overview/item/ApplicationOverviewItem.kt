package com.nasdroid.apps.ui.installed.overview.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.apps.logic.installed.InstalledAppOverview
import com.nasdroid.apps.ui.R
import com.nasdroid.design.MaterialThemeExt

/**
 * Displays information contained in the given [InstalledAppOverview] as a short, reusable list item.
 */
@Composable
fun ApplicationOverviewItem(
    installedAppOverview: InstalledAppOverview,
    onClick: () -> Unit,
    onAppStartRequest: () -> Unit,
    onAppStopRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(installedAppOverview.iconUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Top)
                    .clip(MaterialThemeExt.shapes.small)
            )
            AppInfoText(
                installedAppOverview = installedAppOverview,
                modifier = Modifier.weight(1f)
            )
            AppStateControls(
                appState = installedAppOverview.state,
                onStart = onAppStartRequest,
                onStop = onAppStopRequest,
            )
        }
    }
}

@Composable
internal fun AppInfoText(
    installedAppOverview: InstalledAppOverview,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = installedAppOverview.name,
            style = MaterialThemeExt.typography.titleMedium,
        )
        Text(
            text = if (installedAppOverview.updateAvailable) {
                stringResource(R.string.app_update_available)
            } else {
                stringResource(R.string.app_update_up_to_date)
            },
            style = MaterialThemeExt.typography.bodyMedium,
        )
    }
}

@Composable
internal fun AppStateControls(
    appState: InstalledAppOverview.State,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        AppStateChip(state = appState)
        FilledTonalIconButton(
            onClick = {
                when (appState) {
                    InstalledAppOverview.State.DEPLOYING -> { /* no-op */ }
                    InstalledAppOverview.State.ACTIVE -> onStop()
                    InstalledAppOverview.State.STOPPED -> onStart()
                }
            },
            enabled = when (appState) {
                InstalledAppOverview.State.DEPLOYING -> false
                InstalledAppOverview.State.ACTIVE,
                InstalledAppOverview.State.STOPPED -> true
            }
        ) {
            if (appState == InstalledAppOverview.State.ACTIVE) {
                Icon(Icons.Default.Stop, contentDescription = stringResource(R.string.app_control_stop))
            } else {
                Icon(Icons.Default.PlayArrow, contentDescription = stringResource(R.string.app_control_start))
            }
        }
    }
}

@Preview
@Composable
fun ApplicationOverviewItemPreview() {
    MaterialThemeExt {
        ApplicationOverviewItem(
            installedAppOverview = InstalledAppOverview(
                name = "Jellyfin",
                version = "10.8.10_14.1.9",
                iconUrl = "https://github.com/jellyfin/jellyfin-ux/blob/master/branding/SVG/icon-transparent.svg",
                catalog = "Truenas",
                train = "Community",
                state = InstalledAppOverview.State.ACTIVE,
                updateAvailable = false,
                webPortalUrl = "http://my.jellyfin.local/"
            ),
            onClick = { },
            onAppStartRequest = { },
            onAppStopRequest = { },
            modifier = Modifier.padding(16.dp)
        )
    }
}
