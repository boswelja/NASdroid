package com.nasdroid.storage.ui.pools.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.Capacity.Companion.terabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme
import com.nasdroid.storage.logic.pool.PoolOverview
import com.nasdroid.storage.ui.R

/**
 * Displays information about a given [PoolOverview].
 *
 * @param pool The pool to display information for.
 * @param onShowDetails Called when the user clicked the "See Details" button.
 * @param modifier [Modifier].
 */
@Composable
fun PoolCard(
    pool: PoolOverview,
    onShowDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        onClick = onShowDetails
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = MaterialThemeExt.paddings.large,
                vertical = MaterialThemeExt.paddings.medium
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
        ) {
            Text(
                text = pool.poolName,
                style = MaterialThemeExt.typography.titleLarge,
            )
            StorageUseSummary(
                usedBytes = pool.usedCapacity.toLong(CapacityUnit.BYTE),
                totalBytes = pool.totalCapacity.toLong(CapacityUnit.BYTE),
                modifier = Modifier.fillMaxWidth()
            )
            PoolHealthItem(
                label = { Text(stringResource(R.string.topology_health_title)) },
                healthStatus = pool.topologyHealth
            )
            PoolHealthItem(
                label = { Text(stringResource(R.string.zfs_health_title)) },
                healthStatus = pool.zfsHealth
            )
            PoolHealthItem(
                label = { Text(stringResource(R.string.disks_health_title)) },
                healthStatus = pool.disksHealth
            )
        }
    }
}

/**
 * Displays information about the given [healthStatus] to the user.
 */
@Composable
fun PoolHealthItem(
    label: @Composable () -> Unit,
    healthStatus: PoolOverview.HealthStatus,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (healthStatus.isHealthy) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialThemeExt.colorScheme.secondary
            )
        } else {
            Icon(
                Icons.Default.Cancel,
                contentDescription = null,
                tint = MaterialThemeExt.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.width(MaterialThemeExt.paddings.large))
        Column {
            ProvideTextStyle(MaterialThemeExt.typography.bodyLarge) {
                label()
            }
            ProvideTextStyle(MaterialThemeExt.typography.bodyMedium) {
                if (healthStatus.isHealthy) {
                    Text(
                        text = stringResource(R.string.health_status_healthy),
                        color = MaterialThemeExt.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = healthStatus.unhealthyReason ?: stringResource(R.string.health_status_unknown),
                        color = MaterialThemeExt.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
fun PoolCardPreview() {
    NasDroidTheme {
        PoolCard(
            pool = PoolOverview(
                id = 1,
                poolName = "MyPool",
                totalCapacity = 1.terabytes,
                usedCapacity = 400.gigabytes,
                topologyHealth = PoolOverview.HealthStatus(false, "Disk 1 offline"),
                usageHealth = PoolOverview.HealthStatus(true, null),
                zfsHealth = PoolOverview.HealthStatus(true, null),
                disksHealth = PoolOverview.HealthStatus(true, null)
            ),
            onShowDetails = {}
        )
    }
}
