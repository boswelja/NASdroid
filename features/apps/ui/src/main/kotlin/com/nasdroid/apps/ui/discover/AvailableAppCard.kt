package com.nasdroid.apps.ui.discover

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.apps.logic.discover.AvailableApp
import com.nasdroid.design.MaterialThemeExt
import kotlinx.datetime.Instant

/**
 * An opinionated Material3 Card that is used to display an [AvailableApp]. This Card prefers a
 * landscape aspect ratio.
 */
@Composable
fun AvailableAppCard(
    app: AvailableApp,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(app.iconUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(MaterialThemeExt.shapes.small)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = app.catalogName,
                    style = MaterialThemeExt.typography.labelSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = app.title,
                        style = MaterialThemeExt.typography.titleMedium
                    )
                    Text(
                        text = app.version,
                        style = MaterialThemeExt.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = app.description,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialThemeExt.typography.bodyMedium
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
fun AvailableAppCardPreview() {
    MaterialThemeExt(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        AvailableAppCard(
            app = AvailableApp(
                id = "app",
                title = "App",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                        "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad " +
                        "minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
                        " ex ea commodo consequat.",
                iconUrl = "https://dummyimage.com/250x250/000/fff.png",
                version = "2023.1.1",
                catalogName = "TRUENAS",
                catalogTrain = "community",
                isInstalled = false,
                lastUpdated = Instant.DISTANT_FUTURE
            ),
            modifier = Modifier
                .padding(16.dp)
                .height(128.dp)
                .width(300.dp)
        )
    }
}
