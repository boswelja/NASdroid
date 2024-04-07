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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.Capacity.Companion.terabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.storage.logic.pool.PoolOverview

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
    ElevatedCard(modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PoolHealthItem(
                    label = { Text("Topology") },
                    healthStatus = pool.topologyHealth
                )
                PoolHealthItem(
                    label = { Text("ZFS") },
                    healthStatus = pool.zfsHealth
                )
                PoolHealthItem(
                    label = { Text("Disks") },
                    healthStatus = pool.disksHealth
                )
            }
            FilledTonalButton(
                onClick = onShowDetails,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("See Details")
            }
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
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            ProvideTextStyle(MaterialThemeExt.typography.bodyLarge) {
                label()
            }
            ProvideTextStyle(MaterialThemeExt.typography.bodyMedium) {
                if (healthStatus.isHealthy) {
                    Text(
                        text = "Healthy",
                        color = MaterialThemeExt.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = healthStatus.unhealthyReason ?: "There's a problem",
                        color = MaterialThemeExt.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PoolCardPreview() {
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
