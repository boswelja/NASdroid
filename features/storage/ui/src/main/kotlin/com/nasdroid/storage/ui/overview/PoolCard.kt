package com.nasdroid.storage.ui.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.capacity.Capacity.Companion.gigabytes
import com.nasdroid.capacity.Capacity.Companion.terabytes
import com.nasdroid.capacity.CapacityUnit
import com.nasdroid.storage.logic.pool.PoolOverview

/**
 * Displays information about a given [PoolOverview].
 *
 * @param pool The pool to display information for.
 * @param modifier [Modifier].
 */
@Composable
fun PoolCard(
    pool: PoolOverview,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier) {
        Column {
            PoolOverview(
                name = pool.poolName,
                allocatedBytes = pool.usedCapacity.toLong(CapacityUnit.BYTE),
                totalBytes = pool.totalCapacity.toLong(CapacityUnit.BYTE),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

/**
 * Displays basic details about a pool, such as the pool name & storage usage.
 *
 * @param name The human-readable name of the pool.
 * @param allocatedBytes The number of bytes with data allocated in the pool.
 * @param totalBytes The total number of bytes in the pool.
 * @param modifier [Modifier].
 */
@Composable
fun PoolOverview(
    name: String,
    allocatedBytes: Long,
    totalBytes: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        StorageUseSummary(
            usedBytes = allocatedBytes,
            totalBytes = totalBytes,
            modifier = Modifier.fillMaxWidth()
        )
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
            topologyHealthy = false,
            usageHealthy = true,
            zfsHealthy = true,
            disksHealthy = true
        )
    )
}
