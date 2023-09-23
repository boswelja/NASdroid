package com.nasdroid.storage.logic.pool

import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.api.v2.pool.VDev
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.bytes
import kotlinx.datetime.Instant

class GetPoolDetails(
    private val poolV2Api: PoolV2Api
) {

    suspend operator fun invoke(poolId: Int): Result<PoolDetails> = runCatching {
        val dto = poolV2Api.getPool(poolId)
        PoolDetails(
            id = dto.id,
            name = dto.name,
            usage = PoolDetails.Usage(
                usableCapacity = dto.size.bytes,
                usedCapacity = dto.allocated.bytes,
                availableCapacity = dto.free.bytes
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
                autotrimEnabled = dto.autotrim.rawValue == "on",
                lastScan = if (dto.scan.endTime != null) {
                    PoolDetails.ZfsHealth.LastScan(
                        function = dto.scan.function,
                        finishedAt = Instant.fromEpochMilliseconds(dto.scan.endTime!!),
                        totalErrors = dto.scan.errors
                    )
                } else null
            ),
            diskHealth = PoolDetails.DiskHealth( // TODO
                disksWithAbnormalTemp = 0,
                highestTemp = null,
                lowestTemp = null,
                averageTemp = null,
                failedTests = 0
            )
        )
    }

    private fun List<VDev>.toTopologyDescriptor(): PoolDetails.Topology.TopologyDescriptor? {
        if (isEmpty()) return null
        return PoolDetails.Topology.TopologyDescriptor(
            deviceCount = count(),
            type = PoolDetails.Topology.TopologyDescriptor.Type.RAIDZ1, // TODO Different types?
            driveCount = first().children.count(),
            totalCapacity = sumOf { it.stats.size }.bytes
        )
    }
}

data class PoolDetails(
    val id: Int,
    val name: String,
    val usage: Usage,
    val topology: Topology,
    val zfsHealth: ZfsHealth,
    val diskHealth: DiskHealth,
) {
    data class Usage(
        val usableCapacity: Capacity,
        val usedCapacity: Capacity,
        val availableCapacity: Capacity,
    )

    data class Topology(
        val dataTopology: TopologyDescriptor?,
        val metadataTopology: TopologyDescriptor?,
        val logTopology: TopologyDescriptor?,
        val cacheTopology: TopologyDescriptor?,
        val spareTopology: TopologyDescriptor?,
        val dedupTopology: TopologyDescriptor?,
    ) {
        data class TopologyDescriptor(
            val deviceCount: Int,
            val type: Type,
            val driveCount: Int,
            val totalCapacity: Capacity,
        ) {
            enum class Type {
                RAIDZ1
            }
        }
    }

    data class ZfsHealth(
        val poolStatus: PoolStatus,
        val totalErrors: Int,
        val scheduledScrub: Any?, // TODO
        val autotrimEnabled: Boolean,
        val lastScan: LastScan?,
    ) {
        enum class PoolStatus {
            ONLINE,
        }

        data class LastScan(
            val function: String,
            val finishedAt: Instant,
            val totalErrors: Int,
        )
    }

    data class DiskHealth(
        val disksWithAbnormalTemp: Int,
        val highestTemp: Float?,
        val lowestTemp: Float?,
        val averageTemp: Float?,
        val failedTests: Int,
    )
}
