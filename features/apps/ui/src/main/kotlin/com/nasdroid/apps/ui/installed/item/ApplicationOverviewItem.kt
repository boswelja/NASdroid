package com.nasdroid.apps.ui.installed.item

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.apps.logic.installed.InstalledApplication

/**
 * Displays information contained in the given [InstalledApplication] as a short, reusable list item.
 */
@Composable
fun ApplicationOverviewItem(
    installedApplication: InstalledApplication,
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(installedApplication.iconUrl)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Top)
                    .clip(MaterialTheme.shapes.small)
            )
            AppInfoText(
                installedApplication = installedApplication,
                modifier = Modifier.weight(1f)
            )
            AppStateControls(
                appState = installedApplication.state,
                onStart = onAppStartRequest,
                onStop = onAppStopRequest,
            )
        }
    }
}

@Composable
internal fun AppInfoText(
    installedApplication: InstalledApplication,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = installedApplication.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = if (installedApplication.updateAvailable) {
                "Update available"
            } else {
                "Up to date"
            },
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
internal fun AppStateControls(
    appState: InstalledApplication.State,
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
                    InstalledApplication.State.DEPLOYING -> { /* no-op */ }
                    InstalledApplication.State.ACTIVE -> onStop()
                    InstalledApplication.State.STOPPED -> onStart()
                }
            },
            enabled = when (appState) {
                InstalledApplication.State.DEPLOYING -> false
                InstalledApplication.State.ACTIVE,
                InstalledApplication.State.STOPPED -> true
            }
        ) {
            if (appState == InstalledApplication.State.ACTIVE) {
                Icon(Icons.Default.Stop, contentDescription = "Stop")
            } else {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            }
        }
    }
}

@Preview
@Composable
fun ApplicationOverviewItemPreview() {
    MaterialTheme {
        ApplicationOverviewItem(
            installedApplication = InstalledApplication(
                name = "Jellyfin",
                version = "10.8.10_14.1.9",
                iconUrl = "https://github.com/jellyfin/jellyfin-ux/blob/master/branding/SVG/icon-transparent.svg",
                catalog = "Truenas",
                train = "Community",
                state = InstalledApplication.State.ACTIVE,
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
