package com.nasdroid.storage.ui.pools.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.storage.logic.pool.PoolDetails
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

@Composable
fun ZfsHealthDetails(
    zfsHealth: PoolDetails.ZfsHealth,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ZfsHealthListItem("Pool Status", zfsHealth.poolStatus.toString())
        ZfsHealthListItem("Total ZFS Errors", zfsHealth.totalErrors.toString())
        // TODO Scheduled scrub
        ZfsHealthListItem("Auto TRIM", zfsHealth.isAutotrimEnabled.toString())
        zfsHealth.scan?.let { ScanDetails(it) }
    }
}

@Composable
fun ZfsHealthListItem(
    label: String,
    content: String,
    modifier: Modifier = Modifier
) {
    ListItem(
        overlineContent = { Text(label) },
        headlineContent = { Text(content) },
        modifier = modifier
    )
}

@Composable
fun ScanDetails(
    scan: PoolDetails.ZfsHealth.Scan,
    modifier: Modifier = Modifier
) {

}

@PreviewLightDark
@PreviewFontScale
@Composable
fun ZfsHealthDetailsPreview() {
    NasDroidTheme {
        Surface {
            ZfsHealthDetails(
                zfsHealth = PoolDetails.ZfsHealth(
                    poolStatus = PoolDetails.ZfsHealth.PoolStatus.ONLINE,
                    totalErrors = 0,
                    scheduledScrub = null,
                    isAutotrimEnabled = true,
                    scan = PoolDetails.ZfsHealth.Scan.Finished(
                        functionName = "Scrub",
                        finishedAt = Instant.DISTANT_PAST,
                        errors = 0,
                        duration = 123.seconds
                    )
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
