package com.boswelja.truemanager.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview

/**
 * Displays information contained in the given [ApplicationOverview] as a short, reusable list item.
 */
@Composable
fun ApplicationOverviewItem(
    applicationOverview: ApplicationOverview,
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
                        .data(applicationOverview.iconUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                )
                AppInfoText(
                    applicationOverview = applicationOverview,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (applicationOverview.webPortalUrl != null) {
                    FilledTonalButton(onClick = { /*TODO*/ }) {
                        Text("Web Portal")
                    }
                }
                AppStateControlButton(
                    state = applicationOverview.state,
                    onStart = { /*TODO*/ },
                    onStop = { /*TODO*/ }
                )
                Spacer(Modifier.weight(1f))
                AppControlsOverflowMenu()
            }
        }
    }
}

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

@Composable
internal fun AppControlsOverflowMenu(
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        var expanded by remember { mutableStateOf(false) }
        IconButton(onClick = { expanded = true }, modifier = modifier) {
            Icon(Icons.Default.MoreVert, null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Upgrade") },
                leadingIcon = { Icon(Icons.Default.Upgrade, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Roll Back") },
                leadingIcon = { Icon(Icons.Default.Restore, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = { Icon(Icons.Default.Edit, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Shell") },
                leadingIcon = { Icon(Icons.Default.Terminal, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Logs") },
                leadingIcon = { Icon(Icons.Default.TextSnippet, null) },
                onClick = { /*TODO*/ }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                leadingIcon = { Icon(Icons.Default.Delete, null) },
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Composable
internal fun AppStateControlButton(
    state: ApplicationOverview.State,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        ApplicationOverview.State.STOPPED -> {
            FilledTonalButton(onClick = onStart, modifier = modifier) {
                Text("Start")
            }
        }
        ApplicationOverview.State.ACTIVE -> {
            FilledTonalButton(onClick = onStop, modifier = modifier) {
                Text("Stop")
            }
        }
        ApplicationOverview.State.DEPLOYING -> {
            FilledTonalButton(onClick = onStop, enabled = false, modifier = modifier) {
                Text("Start")
            }
        }
    }
}

@Composable
internal fun AppStateChip(
    state: ApplicationOverview.State,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
    ) {
        when (state) {
            ApplicationOverview.State.STOPPED -> {
                Text(
                    text = "Stopped",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            ApplicationOverview.State.ACTIVE -> {
                Text(
                    text = "Active",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            ApplicationOverview.State.DEPLOYING -> {
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
            applicationOverview = ApplicationOverview(
                name = "Jellyfin",
                version = "10.8.10_14.1.9",
                iconUrl = "https://github.com/jellyfin/jellyfin-ux/blob/master/branding/SVG/icon-transparent.svg",
                catalog = "Truenas",
                train = "Community",
                state = ApplicationOverview.State.ACTIVE,
                updateAvailable = false,
                webPortalUrl = "http://my.jellyfin.local/"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
