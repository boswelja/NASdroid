package com.boswelja.truemanager.apps.ui.available.item

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.boswelja.truemanager.apps.logic.available.AvailableApp

/**
 * Displays information contained in the given [AvailableApp] as a short, reusable list item.
 */
@Composable
fun AvailableAppItem(
    availableApp: AvailableApp,
    onInstallClicked: () -> Unit,
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
                        .data(availableApp.iconUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterVertically)
                )
                AppInfoText(
                    availableApp = availableApp,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                FilledTonalButton(onClick = onInstallClicked) {
                    Text("Install")
                }
            }
        }
    }
}

@Composable
internal fun AppInfoText(
    availableApp: AvailableApp,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = availableApp.title,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = availableApp.version,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@PreviewLightDark
@Composable
fun AvailableAppItemPreview() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        AvailableAppItem(
            availableApp = AvailableApp(
                name = "elastic-search",
                title = "elastic-search",
                version = "8.9.0-1.0.18",
                iconUrl = "https://images.contentstack.io/v3/assets/bltefdd0b53724fa2ce/blt280217a63b82a734/6202d3378b1f312528798412/elastic-logo.svg",
                catalog = "Truenas",
                train = "Charts"
            ),
            onInstallClicked = {},
            modifier = Modifier.widthIn(max = 280.dp)
        )
    }
}
