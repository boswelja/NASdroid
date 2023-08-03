package com.boswelja.truemanager.apps.ui.installed.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.boswelja.truemanager.apps.logic.installed.InstalledApplication

/**
 * Displays information contained in the given [InstalledApplication] as a short, reusable list item.
 */
@Composable
fun ApplicationOverviewItem(
    installedApplication: InstalledApplication,
    onActionClicked: (InstalledAppAction) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
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
                        .align(Alignment.CenterVertically)
                )
                AppInfoText(
                    installedApplication = installedApplication,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (installedApplication.webPortalUrl != null) {
                    FilledTonalButton(onClick = { onActionClicked(InstalledAppAction.WEB_PORTAL) }) {
                        Text("Web Portal")
                    }
                }
                AppStateControlButton(
                    state = installedApplication.state,
                    onStart = { onActionClicked(InstalledAppAction.START) },
                    onStop = { onActionClicked(InstalledAppAction.STOP) }
                )
                Spacer(Modifier.weight(1f))
                AppControlsOverflowMenu(
                    app = installedApplication,
                    onControlClick = {
                        when (it) {
                            AppControl.UPGRADE -> onActionClicked(InstalledAppAction.UPGRADE)
                            AppControl.ROLL_BACK -> onActionClicked(InstalledAppAction.ROLL_BACK)
                            AppControl.EDIT -> onActionClicked(InstalledAppAction.EDIT)
                            AppControl.SHELL -> onActionClicked(InstalledAppAction.SHELL)
                            AppControl.LOGS -> onActionClicked(InstalledAppAction.LOGS)
                            AppControl.DELETE -> onActionClicked(InstalledAppAction.DELETE)
                        }
                    }
                )
            }
        }
    }
}

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

@Composable
internal fun AppStateControlButton(
    state: InstalledApplication.State,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        InstalledApplication.State.STOPPED -> {
            FilledTonalButton(onClick = onStart, modifier = modifier) {
                Text("Start")
            }
        }
        InstalledApplication.State.ACTIVE -> {
            FilledTonalButton(onClick = onStop, modifier = modifier) {
                Text("Stop")
            }
        }
        InstalledApplication.State.DEPLOYING -> {
            FilledTonalButton(onClick = onStop, enabled = false, modifier = modifier) {
                Text("Start")
            }
        }
    }
}

@Composable
internal fun AppStateChip(
    state: InstalledApplication.State,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        when (state) {
            InstalledApplication.State.STOPPED -> {
                Text(
                    text = "Stopped",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledApplication.State.ACTIVE -> {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            InstalledApplication.State.DEPLOYING -> {
                Text(
                    text = "Deploying",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
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
            onActionClicked = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
