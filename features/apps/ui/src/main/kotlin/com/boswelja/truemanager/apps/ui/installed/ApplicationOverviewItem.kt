package com.boswelja.truemanager.apps.ui.installed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.boswelja.truemanager.apps.logic.installed.ApplicationOverview

@Composable
fun ApplicationOverviewItem(
    applicationOverview: ApplicationOverview,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
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
                        .size(64.dp)
                )
                Column(Modifier.weight(1f)) {
                    Text(
                        text = applicationOverview.name,
                        style = MaterialTheme.typography.titleMedium
                    )
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
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    when (applicationOverview.state) {
                        ApplicationOverview.State.STOPPED -> {
                            Text(
                                text = "Stopped",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        ApplicationOverview.State.STARTING -> {
                            Text(
                                text = "Starting",
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
                        ApplicationOverview.State.STOPPING -> {
                            Text(
                                text = "Stopping",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
            Divider(Modifier.padding(vertical = 12.dp))
            Row {
                if (applicationOverview.webPortalUrl != null) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.OpenInNew, null)
                    }
                }
                when (applicationOverview.state) {
                    ApplicationOverview.State.STOPPED -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Start, null)
                        }
                    }
                    ApplicationOverview.State.STARTING -> {
                        IconButton(onClick = { /*TODO*/ }, enabled = false) {
                            Icon(Icons.Default.Start, null)
                        }
                    }
                    ApplicationOverview.State.ACTIVE -> {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Stop, null)
                        }
                    }
                    ApplicationOverview.State.STOPPING -> {
                        IconButton(onClick = { /*TODO*/ }, enabled = false) {
                            Icon(Icons.Default.Stop, null)
                        }
                    }
                }
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