package com.nasdroid.dashboard.ui.overview.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.dashboard.logic.dataloading.system.SystemInformation
import com.nasdroid.dashboard.ui.R
import com.nasdroid.dashboard.ui.overview.common.OverviewItemListItem
import com.nasdroid.dashboard.ui.overview.skeleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/**
 * A Card displaying the given system information. Relevant text in the card is selectable, and last
 * boot time is translated into a live-updating uptime.
 */
@Composable
fun SystemInformationOverview(
    modifier: Modifier = Modifier,
    viewModel: SystemInformationOverviewViewModel = koinViewModel()
) {
    val systemInfo by viewModel.systemInformation.collectAsState()

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
    ) {
        SystemInformationOverview(
            systemInformation = systemInfo?.getOrNull(),
            modifier = modifier,
        )

        if (systemInfo?.exceptionOrNull() != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Something went wrong",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
internal fun SystemInformationOverview(
    systemInformation: SystemInformation?,
    modifier: Modifier = Modifier
) {
    val uptime = systemInformation?.lastBootTime?.collectElapsedSinceAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_version_label)) },
            content = { Text(systemInformation?.version ?: "version") },
            modifier = Modifier.skeleton(systemInformation == null)
        )
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_hostname_label)) },
            content = { Text(systemInformation?.hostname ?: "hostname") },
            modifier = Modifier.skeleton(systemInformation == null)
        )
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_uptime_label)) },
            content = { Text(uptime?.value ?: "uptime") },
            modifier = Modifier.skeleton(systemInformation == null)
        )
    }
}

/**
 * Remembers a [State] that holds a human-readable string representation of the amount of time
 * elapsed since this [LocalDateTime]. For example, if this LocalDateTime points to 1 hour 30
 * minutes ago, the State will be "1h 30m". This State will update every second to keep the counter
 * rolling.
 */
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

private suspend fun repeatIndefinitely(interval: Duration, block: () -> Unit) {
    while (coroutineContext.isActive) {
        val execTime = measureTime(block)
        delay(interval - execTime)
    }
}

@Preview(showBackground = true)
@Composable
fun SystemInformationOverviewPreview() {
    SystemInformationOverview(
        systemInformation = SystemInformation(
            version = "TrueNAS-SCALE-22.12.2",
            hostname = "truenas",
            lastBootTime = LocalDateTime(2023, 3, 1, 10, 33)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}
