package com.boswelja.truemanager.dashboard.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Composable
fun SystemInformationCard(
    systemInformation: SystemInformation,
    modifier: Modifier = Modifier
) {
    val uptime by systemInformation.lastBootTime.collectElapsedSinceAsState()
    Card(modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "System Information",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            SystemInformationItem(
                labelContent = { Text("Platform") },
                content = { Text(systemInformation.platform) }
            )
            SystemInformationItem(
                labelContent = { Text("Version") },
                content = { Text(systemInformation.version) }
            )
            SystemInformationItem(
                labelContent = { Text("Hostname") },
                content = { Text(systemInformation.hostname) }
            )
            SystemInformationItem(
                labelContent = { Text("Uptime") },
                content = { Text(uptime) }
            )
        }
    }
}

@Composable
fun SystemInformationItem(
    labelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelLarge,
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
            content = labelContent
        )
        SelectionContainer {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyLarge,
                LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                content = content
            )
        }
    }
}

@Composable
fun LocalDateTime.collectElapsedSinceAsState(
    clock: Clock = Clock.System,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): State<String> {
    val timeElapsed = remember {
        mutableStateOf("0s")
    }

    LaunchedEffect(this) {
        val timeAsInstant = this@collectElapsedSinceAsState.toInstant(timeZone)
        repeatIndefinitely(1.seconds) {
            val elapsedDuration = clock.now().minus(timeAsInstant)
            val elapsedDurationRounded = elapsedDuration.inWholeSeconds.seconds
            timeElapsed.value = elapsedDurationRounded.toString()
        }
    }

    return timeElapsed
}

@OptIn(ExperimentalTime::class)
private suspend fun repeatIndefinitely(interval: Duration, block: () -> Unit) {
    while (coroutineContext.isActive) {
        val execTime = measureTime(block)
        delay(interval - execTime)
    }
}

data class SystemInformation(
    val platform: String,
    val version: String,
    val hostname: String,
    val lastBootTime: LocalDateTime
)

@Preview
@Composable
fun SystemInformationCardPreview() {
    SystemInformationCard(
        systemInformation = SystemInformation(
            platform = "Generic",
            version = "TrueNAS-SCALE-22.12.2",
            hostname = "truenas",
            lastBootTime = LocalDateTime(2023, 3, 1, 10, 33)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

