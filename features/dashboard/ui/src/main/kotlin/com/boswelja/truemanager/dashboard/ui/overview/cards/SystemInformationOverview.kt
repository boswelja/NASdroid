package com.boswelja.truemanager.dashboard.ui.overview.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.boswelja.truemanager.dashboard.logic.dataloading.DashboardData
import com.boswelja.truemanager.dashboard.ui.R
import com.boswelja.truemanager.dashboard.ui.overview.cards.common.OverviewItemListItem
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

/**
 * A Card displaying the given system information. Relevant text in the card is selectable, and last
 * boot time is translated into a live-updating uptime.
 */
@Composable
fun SystemInformationOverview(
    data: DashboardData.SystemInformationData,
    modifier: Modifier = Modifier
) {
    val uptime by data.lastBootTime.collectElapsedSinceAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_version_label)) },
            content = { Text(data.version) }
        )
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_hostname_label)) },
            content = { Text(data.hostname) }
        )
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.system_info_uptime_label)) },
            content = { Text(uptime) }
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

@OptIn(ExperimentalTime::class)
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
        data = DashboardData.SystemInformationData(
            version = "TrueNAS-SCALE-22.12.2",
            hostname = "truenas",
            lastBootTime = LocalDateTime(2023, 3, 1, 10, 33),
            uid = 0
        ),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    )
}
