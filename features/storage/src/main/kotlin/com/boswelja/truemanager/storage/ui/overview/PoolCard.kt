package com.boswelja.truemanager.storage.ui.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.core.api.v2.pool.Pool
import com.boswelja.truemanager.core.api.v2.pool.Scan
import com.boswelja.truemanager.core.api.v2.pool.Topology
import com.boswelja.truemanager.storage.R
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

/**
 * Displays information about a given [Pool].
 *
 * @param pool The pool to display information for.
 * @param modifier [Modifier].
 */
@Composable
fun PoolCard(
    pool: Pool,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    val dividerPadding by animateDpAsState(targetValue = if (expanded) 24.dp else 0.dp, label = "Card divider padding")
    OutlinedCard(modifier) {
        Column {
            PoolOverview(
                name = pool.name,
                capacity = pool.capacity,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp)
            )
            ScanDetails(
                scan = pool.scan,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dividerPadding)
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                TopologyDetails(
                    topology = pool.topology,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp)
                )
            }
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (expanded) {
                        stringResource(R.string.button_show_less)
                    } else {
                        stringResource(R.string.button_show_more)
                    }
                )
            }
        }
    }
}

/**
 * Displays basic details about a pool, such as the pool name & storage usage.
 *
 * @param name The human-readable name of the pool.
 * @param capacity Capacity information for the pool.
 * @param modifier [Modifier].
 */
@Composable
fun PoolOverview(
    name: String,
    capacity: Pool.Capacity,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        StorageUseSummary(
            usedBytes = capacity.allocatedBytes,
            totalBytes = capacity.sizeBytes,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PoolCardPreview() {
    PoolCard(
        pool = Pool(
            autotrim = Pool.Autotrim(true, "LOCAL", true),
            fragmentation = 4,
            freeing = 0,
            guid = "guid",
            healthy = true,
            id = 1,
            isDecrypted = true,
            name = "My Pool",
            path = "/mnt/my-pool",
            scan = Scan(
                bytesIssued = 0,
                bytesProcessed = 10,
                bytesToProcess = 100,
                endTime = null,
                errors = 0,
                function = "SCRUB",
                pause = null,
                percentage = 100.0,
                startTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                state = "COMPLETED",
                remainingTime = Duration.ZERO
            ),
            status = "ONLINE",
            statusDetail = null,
            topology = Topology(
                cache = listOf(),
                data = listOf(),
                dedup = listOf(),
                log = listOf(),
                spare = listOf(),
                special = listOf()
            ),
            warning = false,
            capacity = Pool.Capacity(
                allocatedBytes = 1000,
                freeBytes = 1000,
                sizeBytes = 2000,
            ),
            encryption = Pool.Encryption(
                encrypt = 0,
                encryptkey = "",
                encryptkeyPath = null
            )
        )
    )
}
