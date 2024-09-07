package com.nasdroid.storage.logic.pool

import com.boswelja.capacity.Capacity
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.api.v2.pool.PoolStatus
import com.nasdroid.api.v2.pool.PoolV2Api

/**
 * Gets a list of all pools and their status. See [invoke] for details.
 */
class GetPoolOverviews(
    private val poolV2Api: PoolV2Api
) {

    /**
     * Retrieves a list of [Pool]s from the server.
     */
    suspend operator fun invoke(): Result<List<PoolOverview>> {
        return try {
            val pools = poolV2Api.getPools()
            val result = pools.map { pool ->
                PoolOverview(
                    id = pool.id,
                    poolName = pool.name,
                    totalCapacity = pool.size!!.bytes,
                    usedCapacity = pool.allocated!!.bytes,
                    topologyHealth = pool.calculateTopologyHealth(),
                    usageHealth = pool.calculateUsageHealth(),
                    zfsHealth = PoolOverview.ZfsHealth.Healthy, // TODO
                    disksHealth = PoolOverview.DisksHealth.Healthy // TODO
                )
            }
            Result.success(result)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }

    private fun Pool.calculateTopologyHealth(): PoolOverview.TopologyHealth {
        var degradedVdevs = 0
        var offlineVdevs = 0
        topology.data.forEach {
            when (it.status) {
                PoolStatus.Online -> { /* no-op */ }
                PoolStatus.Degraded -> degradedVdevs++
                PoolStatus.Offline -> offlineVdevs++
            }
        }
        return if (offlineVdevs > 0) {
            PoolOverview.TopologyHealth.Offline
        } else if (degradedVdevs > 0) {
            PoolOverview.TopologyHealth.Degraded(degradedVdevs)
        } else {
            PoolOverview.TopologyHealth.Healthy
        }
    }

    private fun Pool.calculateUsageHealth(): PoolOverview.UsageHealth {
        val freeCapacity = free?.bytes ?: 0.bytes
        val warningBuffer = 10.gigabytes // TODO Where should this come from?
        return if (freeCapacity <= 0.bytes) {
            PoolOverview.UsageHealth.Full
        } else if (freeCapacity < warningBuffer) {
            PoolOverview.UsageHealth.LowFreeSpace
        } else {
            PoolOverview.UsageHealth.Healthy
        }
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
    val topologyHealth: TopologyHealth,
    val usageHealth: UsageHealth,
    val zfsHealth: ZfsHealth,
    val disksHealth: DisksHealth,
) {

    /**
     * Encapsulates all topology health descriptors.
     */
    sealed interface TopologyHealth {
        /**
         * All VDEVs are healthy.
         */
        data object Healthy : TopologyHealth

        /**
         * Some VDEVs are degraded, but the pool is still operational. See [degradedVdevs] for a count.
         */
        data class Degraded(val degradedVdevs: Int): TopologyHealth

        /**
         * Some VDEVs are degraded, causing the pool to be inaccessible.
         */
        data object Offline : TopologyHealth
    }

    /**
     * Encapsulates all pool usage health descriptors.
     */
    enum class UsageHealth {
        Healthy,
        LowFreeSpace,
        Full
    }

    /**
     * Encapsulates all ZFS health descriptors.
     */
    enum class ZfsHealth {
        Healthy
        // TODO
    }

    /**
     * Encapsulates the overall health of all disks in a pool.
     */
    enum class DisksHealth {
        Healthy
        // TODO
    }
}
