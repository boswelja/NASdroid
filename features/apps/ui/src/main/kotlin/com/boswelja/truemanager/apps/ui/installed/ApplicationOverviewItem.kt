package com.boswelja.truemanager.apps.ui.installed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
            Row {
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
                Column {
                    Text(applicationOverview.name)
                    Text(applicationOverview.version)
                }
            }
            Divider()
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