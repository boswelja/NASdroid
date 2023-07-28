package com.boswelja.truemanager.core.logviewer

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.logviewer.parser.DefaultLogParser
import com.boswelja.truemanager.core.logviewer.parser.LogLevel
import com.boswelja.truemanager.core.logviewer.parser.LogLine
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * A Composable that can be used to display log-style text.
 */
@Composable
fun LogViewer(
    logContents: List<String>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    logColors: LogColors = rememberMaterial3LogColors()
) {
    val fontFamily = rememberFontFamily()
    val lines by remember(logContents) {
        val logParser = DefaultLogParser()
        derivedStateOf { logParser.parseLines(logContents) }
    }
    ProvideTextStyle(value = LocalTextStyle.current.copy(fontFamily = fontFamily)) {
        LazyColumn(modifier = modifier, contentPadding = contentPadding) {
            items(
                items = lines,
                key = { it.hashCode() }
            ) { logLine ->
                LogText(
                    logLine = logLine,
                    logColors = logColors,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
internal fun LogText(
    logLine: LogLine,
    logColors: LogColors,
    modifier: Modifier = Modifier
) {
    val logColor = when (logLine.level) {
        LogLevel.Debug ->logColors.debug
        LogLevel.Info -> logColors.info
        LogLevel.Warning -> logColors.warn
        LogLevel.Error -> logColors.error
        null -> LocalContentColor.current
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = logLine.timestamp.toLocalDateTime(TimeZone.UTC).time.toString().dropLast(3),
            color = logColors.timestamp,
        )
        LogLevelIndicator(
            logLevel = logLine.level,
            textColor = logColors.levelIndicator,
            backgroundColor = logColor
        )
        Text(
            text = logLine.content,
            color = logColor
        )
    }
}

@Composable
internal fun LogLevelIndicator(
    logLevel: LogLevel?,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val height = placeable.height
                val contentStart = (height / 2) - (placeable.width / 2)
                layout(height, height) {
                    placeable.placeRelative(contentStart, 0)
                }
            }
    ) {
        Text(
            text = logLevel?.name?.first()?.toString() ?: "?",
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
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
                "2023-07-24 08:39:32.538972+00:002023/07/24 08:39:32.538640 [warn] AdGuard Home updates are disabled",
                "2023-07-24 08:39:32.540734+00:002023/07/24 08:39:32.540604 [debug] tls: using default ciphers",
                "2023-07-24 08:39:32.548932+00:002023/07/24 08:39:32.548823 [info] safesearch default: disabled",
                "2023-07-24 08:39:32.553088+00:002023/07/24 08:39:32.552956 [debug] Initializing auth module: /opt/adguardhome/work/data/sessions.db",
                "2023-07-24 08:39:32.553828+00:002023/07/24 08:39:32.553718 [debug] auth: initialized.  users:1  sessions:2",
                "2023-07-24 08:39:32.553865+00:002023/07/24 08:39:32.553768 [debug] web: initializing",
                "2023-07-24 08:39:32.737805+00:002023/07/24 08:39:32.737657 [debug] dnsproxy: cache: enabled, size 4096 b",
                "2023-07-24 08:39:32.737850+00:002023/07/24 08:39:32.737677 [debug] dnsproxy: max goroutines is set to 300",
                "2023-07-24 08:39:32.745533+00:002023/07/24 08:39:32.745355 [info] AdGuard Home is available at the following addresses:",
                "2023-07-24 08:39:32.746465+00:002023/07/24 08:39:32.746278 [info] go to http://127.0.0.1:10232",
                "2023-07-24 08:39:32.746526+00:002023/07/24 08:39:32.746289 [info] go to http://[::1]:10232",
                "2023-07-24 08:39:35.870714+00:002023/07/24 08:39:35.870474 [debug] dnsproxy: starting dns proxy server",
                "2023-07-24 08:39:35.870796+00:002023/07/24 08:39:35.870642 [error] The server is configured to refuse ANY requests",
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
