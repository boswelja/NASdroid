package com.nasdroid.apps.ui.installed.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.apps.logic.installed.InstalledAppDetails
import com.nasdroid.core.urllauncher.rememberUrlLauncher

@Composable
fun ApplicationInfo(
    installedAppDetails: InstalledAppDetails,
    modifier: Modifier = Modifier
) {
    val urlLauncher = rememberUrlLauncher()

    Column(modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(installedAppDetails.iconUrl)
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .align(Alignment.CenterHorizontally)
                .clip(MaterialTheme.shapes.small)
        )
        Text("Name: ${installedAppDetails.name}")
        Text("App Version: ${installedAppDetails.appVersion}")
        Text("Chart Version: ${installedAppDetails.chartVersion}")
        Text("Last Updated: N/A")
        Text("Sources:")
        installedAppDetails.sources.forEach { url ->
            ClickableText(text = AnnotatedString(url)) {
                urlLauncher.launchUrl(url)
            }
        }
        Text("Developer: N/A")
        Text("Catalog: ${installedAppDetails.catalog}")
        Text("Train: ${installedAppDetails.train}")
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationInfoPreview() {
    MaterialTheme {
        ApplicationInfo(
            installedAppDetails = InstalledAppDetails(
                name = "adguard-home",
                iconUrl = "https://media.sys.truenas.net/apps/adguard-home/icons/icon.svg",
                description = "\n# Welcome to TrueNAS SCALE\n" +
                        "Thank you for installing AdGuard Home App.\n\n\n" +
                        "# Documentation\n" +
                        "Documentation for this app can be found at https://www.truenas.com/docs.\n" +
                        "# Bug reports\n" +
                        "If you find a bug in this app, please file an issue at https://ixsystems.atlassian.net\n\n",
                appVersion = "0.107.43",
                chartVersion = "1.0.32",
                lastUpdated = null,
                sources = listOf(
                    "https://github.com/AdguardTeam/AdGuardHome",
                    "https://github.com/truenas/charts/tree/master/community/adguard-home",
                    "https://hub.docker.com/r/adguard/adguardhome"
                ),
                developer = null,
                catalog = "TrueNAS",
                train = "community",
                state = InstalledAppDetails.State.ACTIVE,
                updateAvailable = true,
                webPortalUrl = "http://adguard.local/"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}