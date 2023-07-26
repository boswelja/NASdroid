package com.boswelja.truemanager.core.logviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LogViewer(
    logContents: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val fontFamily = rememberFontFamily()

    val maxLineCount = remember(logContents) { (logContents.size + 1).toString() }
    ProvideTextStyle(value = LocalTextStyle.current.copy(fontFamily = fontFamily)) {
        LazyColumn(modifier = modifier, contentPadding = contentPadding) {
            itemsIndexed(logContents) { index, logLine ->
                val lineNumber = remember(index, maxLineCount) {
                    val lineNumStr = (index + 1).toString()
                    val blankSpaceCount = maxLineCount.length - lineNumStr.length
                    buildString {
                        (0..blankSpaceCount).forEach { _ ->
                            append("\u0020")
                        }
                        append(lineNumStr)
                    }
                }
                val background = if (index % 2 == 0) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(background)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(lineNumber)
                    VerticalDivider()
                    Text(logLine)
                }
            }
        }
    }
}

@Composable
internal fun rememberFontFamily(): FontFamily {
    return remember {
        val provider = GoogleFont.Provider(
            providerAuthority = "com.google.android.gms.fonts",
            providerPackage = "com.google.android.gms",
            certificates = R.array.com_google_android_gms_fonts_certs
        )
        val fontName = GoogleFont("Roboto Mono")
        FontFamily(Font(fontName, provider))
    }
}

@Suppress("MaxLineLength")
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LogViewerPreview() {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()) {
        LogViewer(
            logContents = listOf(
                "2023-07-24 08:39:32.538870+00:002023/07/24 08:39:32.538561 [info] AdGuard Home, version v0.107.34",
                "2023-07-24 08:39:32.538972+00:002023/07/24 08:39:32.538640 [info] AdGuard Home updates are disabled",
                "2023-07-24 08:39:32.540734+00:002023/07/24 08:39:32.540604 [info] tls: using default ciphers",
                "2023-07-24 08:39:32.548932+00:002023/07/24 08:39:32.548823 [info] safesearch default: disabled",
                "2023-07-24 08:39:32.553088+00:002023/07/24 08:39:32.552956 [info] Initializing auth module: /opt/adguardhome/work/data/sessions.db",
                "2023-07-24 08:39:32.553828+00:002023/07/24 08:39:32.553718 [info] auth: initialized.  users:1  sessions:2",
                "2023-07-24 08:39:32.553865+00:002023/07/24 08:39:32.553768 [info] web: initializing",
                "2023-07-24 08:39:32.737805+00:002023/07/24 08:39:32.737657 [info] dnsproxy: cache: enabled, size 4096 b",
                "2023-07-24 08:39:32.737850+00:002023/07/24 08:39:32.737677 [info] dnsproxy: max goroutines is set to 300",
                "2023-07-24 08:39:32.745533+00:002023/07/24 08:39:32.745355 [info] AdGuard Home is available at the following addresses:",
                "2023-07-24 08:39:32.746465+00:002023/07/24 08:39:32.746278 [info] go to http://127.0.0.1:10232",
                "2023-07-24 08:39:32.746526+00:002023/07/24 08:39:32.746289 [info] go to http://[::1]:10232",
                "2023-07-24 08:39:35.870714+00:002023/07/24 08:39:35.870474 [info] dnsproxy: starting dns proxy server",
                "2023-07-24 08:39:35.870796+00:002023/07/24 08:39:35.870642 [info] The server is configured to refuse ANY requests",
                "2023-07-24 08:39:35.870813+00:002023/07/24 08:39:35.870656 [info] dnsproxy: cache: enabled, size 16777216 b",
                "2023-07-24 08:39:35.870827+00:002023/07/24 08:39:35.870669 [info] dnsproxy: max goroutines is set to 300",
                "2023-07-24 08:39:35.871072+00:002023/07/24 08:39:35.870926 [info] dnsproxy: creating udp server socket 0.0.0.0:53",
                "2023-07-24 08:39:35.871521+00:002023/07/24 08:39:35.871369 [info] dnsproxy: listening to udp://[::]:53",
                "2023-07-24 08:39:35.871565+00:002023/07/24 08:39:35.871416 [info] dnsproxy: creating tcp server socket 0.0.0.0:53",
                "2023-07-24 08:39:35.871600+00:002023/07/24 08:39:35.871484 [info] dnsproxy: listening to tcp://[::]:53",
                "2023-07-24 08:39:35.874001+00:002023/07/24 08:39:35.873837 [info] dnsproxy: entering udp listener loop on [::]:53",
                "2023-07-24 08:39:35.875537+00:002023/07/24 08:39:35.875383 [info] dnsproxy: entering tcp listener loop on [::]:53"
            )
        )
    }
}
