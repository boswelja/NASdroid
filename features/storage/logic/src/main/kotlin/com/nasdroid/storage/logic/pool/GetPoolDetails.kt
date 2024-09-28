package com.nasdroid.storage.logic.pool

import com.boswelja.capacity.Capacity
import com.boswelja.capacity.Capacity.Companion.bytes
import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.api.v2.pool.PoolV2Api
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Retrieves information about a single storage pool. See [invoke] for details.
 */
class GetPoolDetails(
    private val poolV2Api: PoolV2Api
) {

    /**
     * Gets a [PoolDetails] for the storage pool with the give ID.
     */
    suspend operator fun invoke(poolId: Int): Result<PoolDetails> {
        return try {
            val dto = poolV2Api.getPool(poolId)
            val result = PoolDetails(
                id = dto.id,
                name = dto.name,
                usage = PoolDetails.Usage(
                    usableCapacity = dto.size!!.bytes,
                    usedCapacity = dto.allocated!!.bytes,
                    availableCapacity = dto.free!!.bytes
                ),
                topology = PoolDetails.Topology(
                    dataTopology = dto.topology.data.toTopologyDescriptor(),
                    metadataTopology = dto.topology.special.toTopologyDescriptor(),
                    logTopology = dto.topology.log.toTopologyDescriptor(),
                    cacheTopology = dto.topology.cache.toTopologyDescriptor(),
                    spareTopology = dto.topology.spare.toTopologyDescriptor(),
                    dedupTopology = dto.topology.dedup.toTopologyDescriptor()
                ),
                zfsHealth = PoolDetails.ZfsHealth(
                    poolStatus = PoolDetails.ZfsHealth.PoolStatus.ONLINE, // TODO
                    totalErrors = dto.scan.errors,
                    scheduledScrub = null, // TODO
                    isAutotrimEnabled = dto.autotrim.rawValue == "on",
                    scan = when (dto.scan.state) {
                        Pool.Scan.ScanState.Finished -> {
                            val end = Instant.fromEpochMilliseconds(dto.scan.endTime!!)
                            PoolDetails.ZfsHealth.Scan.Finished(
                                functionName = dto.scan.function,
                                errors = dto.scan.errors,
                                finishedAt = end,
                                duration = end - Instant.fromEpochMilliseconds(dto.scan.startTime)
                            )
                        }
                        Pool.Scan.ScanState.Scanning -> {
                            PoolDetails.ZfsHealth.Scan.InProgress(
                                functionName = dto.scan.function,
                                errors = dto.scan.errors,
                                startedAt =  Instant.fromEpochMilliseconds(dto.scan.startTime),
                                remaining = dto.scan.totalSecsLeft!!.seconds,
                                pausedAt = dto.scan.pause?.let { Instant.fromEpochMilliseconds(it) }
                            )
                        }
                        Pool.Scan.ScanState.Cancelled -> {
                            val end = Instant.fromEpochMilliseconds(dto.scan.endTime!!)
                            PoolDetails.ZfsHealth.Scan.Cancelled(
                                functionName = dto.scan.function,
                                errors = dto.scan.errors,
                                finishedAt = end,
                                duration = end - Instant.fromEpochMilliseconds(dto.scan.startTime)
                            )
                        }
                    }
                ),
                diskHealth = PoolDetails.DiskHealth( // TODO
                    disksWithAbnormalTemp = 0,
                    highestTemp = null,
                    lowestTemp = null,
                    averageTemp = null,
                    failedTests = 0
                )
            )
            Result.success(result)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }

    private fun List<Pool.VDev>.toTopologyDescriptor(): PoolDetails.Topology.TopologyDescriptor? {
        if (isEmpty()) return null
        return PoolDetails.Topology.TopologyDescriptor(
            deviceCount = count(),
            type = PoolDetails.Topology.TopologyDescriptor.Type.RAIDZ1, // TODO Different types?
            driveCount = first().children.count(),
            totalCapacity = sumOf { it.stats.size }.bytes
        )
    }
}

/**
 * Describes a single storage pool.
 *
 * @property id The unique ID of the storage pool.
 * @property name The user-specified pool name.
 * @property usage A summary of pool data usage. See [Usage].
 * @property topology A summary of pool topology. See [Topology].
 * @property zfsHealth A summary of filesystem health. See [ZfsHealth].
 * @property diskHealth A summary of disk health. See [DiskHealth].
 */
data class PoolDetails(
    val id: Int,
    val name: String,
    val usage: Usage,
    val topology: Topology,
    val zfsHealth: ZfsHealth,
    val diskHealth: DiskHealth,
) {

    /**
     * A summary of pool data usage.
     *
     * @property usableCapacity The total usable capacity of the pool.
     * @property usedCapacity The amount of data allocated in the pool.
     * @property availableCapacity The remaining "free" capacity of the pool.
     */
    data class Usage(
        val usableCapacity: Capacity,
        val usedCapacity: Capacity,
        val availableCapacity: Capacity,
    )

    /**
     * A summary of pool topology.
     *
     * @property dataTopology Describes the topology of the "data" dataset.
     * @property metadataTopology Describes the topology of the "metadata" dataset.
     * @property logTopology Describes the topology of the "log" dataset.
     * @property cacheTopology Describes the topology of the "cache" dataset.
     * @property spareTopology Describes the topology of the "spare" dataset.
     * @property dedupTopology Describes the topology of the "dedup" dataset.
     */
    data class Topology(
        val dataTopology: TopologyDescriptor?,
        val metadataTopology: TopologyDescriptor?,
        val logTopology: TopologyDescriptor?,
        val cacheTopology: TopologyDescriptor?,
        val spareTopology: TopologyDescriptor?,
        val dedupTopology: TopologyDescriptor?,
    ) {
        /**
         * Describes the topology of a dataset.
         *
         * @property deviceCount The number of direct descendant devices contained in this dataset.
         * @property type The dataset type.
         * @property driveCount The total number of physical drives in this dataset.
         * @property totalCapacity The total available capacity in this dataset.
         */
        data class TopologyDescriptor(
            val deviceCount: Int,
            val type: Type,
            val driveCount: Int,
            val totalCapacity: Capacity,
        ) {
            /**
             * All possible types for a dataset.
             */
            enum class Type {
                RAIDZ1
            }
        }
    }

    /**
     * Describes the health of a pool filesystem.
     *
     * @property poolStatus The status of the pool.
     * @property totalErrors The total number of filesystem errors detected.
     * @property scheduledScrub TODO
     * @property isAutotrimEnabled Whether TRIM happens automatically.
     * @property scan A scan on this pool that is either underway, or finished.
     */
    data class ZfsHealth(
        val poolStatus: PoolStatus,
        val totalErrors: Int,
        val scheduledScrub: Any?, // TODO
        val isAutotrimEnabled: Boolean,
        val scan: Scan?,
    ) {

        /**
         * All possible statuses for a pool.
         */
        enum class PoolStatus {
            ONLINE,
        }

        sealed interface Scan {

            val functionName: String
            val errors: Int

            data class InProgress(
                override val functionName: String,
                override val errors: Int,
                val startedAt: Instant,
                val remaining: Duration,
                val pausedAt: Instant?
            ) : Scan

            data class Finished(
                override val functionName: String,
                override val errors: Int,
                val finishedAt: Instant,
                val duration: Duration
            ) : Scan

            data class Cancelled(
                override val functionName: String,
                override val errors: Int,
                val finishedAt: Instant,
                val duration: Duration
            ) : Scan
        }
    }

    /**
     * Describes the health of all disks in a pool.
     *
     * @property disksWithAbnormalTemp The number of disks that are outside their safe operating
     * temperature.
     * @property highestTemp The highest temperature of any disk at this instant.
     * @property lowestTemp The lowest temperature of any disk at this instant.
     * @property averageTemp The average temperature of all disks at this instant.
     * @property failedTests The number of disks that failed tests.
     */
    data class DiskHealth(
        val disksWithAbnormalTemp: Int,
        val highestTemp: Float?,
        val lowestTemp: Float?,
        val averageTemp: Float?,
        val failedTests: Int,
    )
}
