package com.nasdroid.storage.logic.pool

import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.bytes

/**
 * Gets a list of all pools and their status. See [invoke] for details.
 */
class GetPoolOverviews(
    private val poolV2Api: PoolV2Api
) {

    /**
     * Retrieves a list of [Pool]s from the server.
     */
    suspend operator fun invoke(): Result<List<PoolOverview>> = runCatching {
        val pools = poolV2Api.getPools()

        pools.map { pool ->
            PoolOverview(
                id = pool.id,
                poolName = pool.name,
                totalCapacity = pool.size.bytes,
                usedCapacity = pool.allocated.bytes,
                topologyHealthy = true, // TODO
                usageHealthy = pool.healthy,
                zfsHealthy = true, // TODO
                disksHealthy = true, // TODO
            )
        }
    }
}

data class PoolOverview(
    val id: Int,
    val poolName: String,
    val totalCapacity: Capacity,
    val usedCapacity: Capacity,
    val topologyHealthy: Boolean,
    val usageHealthy: Boolean,
    val zfsHealthy: Boolean,
    val disksHealthy: Boolean,
)
