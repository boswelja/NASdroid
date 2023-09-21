package com.nasdroid.storage.logic.pool

import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.api.v2.pool.Topology
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
                topologyHealth = PoolOverview.HealthStatus(pool.topology.isHealthy(), null), // TODO Reason
                usageHealth = PoolOverview.HealthStatus(pool.healthy, null),
                zfsHealth = PoolOverview.HealthStatus(pool.scan.errors <= 0, null),
                disksHealth = PoolOverview.HealthStatus(true, null), // TODO
            )
        }
    }

    private fun Topology.isHealthy(): Boolean {
        // TODO This should ideally be a separate use case
        return data.all { it.status == "ONLINE" } &&
                special.all { it.status == "ONLINE" } &&
                cache.all { it.status == "ONLINE" } &&
                log.all { it.status == "ONLINE" } &&
                spare.all { it.status == "ONLINE" } &&
                dedup.all { it.status == "ONLINE" }
    }
}

data class PoolOverview(
    val id: Int,
    val poolName: String,
    val totalCapacity: Capacity,
    val usedCapacity: Capacity,
    val topologyHealth: HealthStatus,
    val usageHealth: HealthStatus,
    val zfsHealth: HealthStatus,
    val disksHealth: HealthStatus,
) {
    data class HealthStatus(
        val isHealthy: Boolean,
        val unhealthyReason: String?
    )
}
