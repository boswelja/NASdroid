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
        return data.all { it.status == STATUS_ONLINE } &&
                special.all { it.status == STATUS_ONLINE } &&
                cache.all { it.status == STATUS_ONLINE } &&
                log.all { it.status == STATUS_ONLINE } &&
                spare.all { it.status == STATUS_ONLINE } &&
                dedup.all { it.status == STATUS_ONLINE }
    }

    companion object {
        private const val STATUS_ONLINE = "ONLINE"
    }
}

/**
 * Describes the state of a single storage pool.
 *
 * @property id The unique identifier of the pool.
 * @property poolName The name of the pool.
 * @property totalCapacity The total [Capacity] this pool can hold.
 * @property usedCapacity The [Capacity] of allocated data in the pool.
 * @property topologyHealth Describes the health of pool topology.
 * @property usageHealth Describes the health of pool usage.
 * @property zfsHealth Describes the health of the pool filesystem.
 * @property disksHealth Describes the health of disks in the pool.
 */
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

    /**
     * Describes the health of an item in the pool.
     *
     * @property isHealthy Whether the item is healthy.
     * @property unhealthyReason Why the pool is unhealthy, if available.
     */
    data class HealthStatus(
        val isHealthy: Boolean,
        val unhealthyReason: String?
    )
}
