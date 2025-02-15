package com.nasdroid.dashboard.logic.dataloading

import com.boswelja.bitrate.Bitrate
import com.boswelja.bitrate.Bitrate.Companion.kilobits
import com.boswelja.capacity.Capacity
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.percentage.Percentage
import com.boswelja.percentage.Percentage.Companion.percent
import com.boswelja.temperature.Temperature
import com.nasdroid.api.websocket.reporting.ReportingApi
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class GetRealtimeStats(
    private val reportingApi: ReportingApi
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        interval: Duration = 2.seconds
    ): Flow<StrongResult<RealtimeStats, GetRealtimeStatsError>> = reportingApi.realtime(interval.inWholeSeconds.toInt())
        .mapLatest { utilisation ->
            val stats = RealtimeStats(
                cpu = RealtimeStats.Cpu(
                    utilisation = 50.percent,
                    cores = emptyList()
                ),
                memory = RealtimeStats.Memory(
                    appsUsage = utilisation.memory.apps.bytes,
                    arcUsage = utilisation.memory.arc.bytes,
                    buffersUsage = utilisation.memory.buffers.bytes,
                    cacheUsage = utilisation.memory.cache.bytes,
                    pageTables = utilisation.memory.pageTables.bytes,
                    slabCache = utilisation.memory.slabCache.bytes,
                    unused = utilisation.memory.unused.bytes
                ),
                disks = RealtimeStats.Disks(
                    read = utilisation.disks.readBytes.kilobits,
                    write = utilisation.disks.readBytes.kilobits,
                    readOps = utilisation.disks.readOps,
                    writeOps = utilisation.disks.writeOps
                ),
                interfaces = emptyList(),
                zfs = RealtimeStats.Zfs(
                    arcSize = utilisation.zfs.arcSize.bytes,
                    arcMaxSize = utilisation.zfs.arcMaxSize.bytes,
                    cacheHits = utilisation.zfs.cacheHitRatio.percent
                )
            )
            StrongResult.success(stats)
        }
}

sealed interface GetRealtimeStatsError {

}

data class RealtimeStats(
    val cpu: Cpu,
    val memory: Memory,
    val disks: Disks,
    val interfaces: List<Interface>,
    val zfs: Zfs,
) {
    data class Cpu(
        val utilisation: Percentage,
        val cores: List<Core>
    ) {
        data class Core(
            val utilisation: Percentage,
            val temperature: Temperature
        )
    }

    data class Memory(
        val appsUsage: Capacity,
        val arcUsage: Capacity,
        val buffersUsage: Capacity,
        val cacheUsage: Capacity,
        val pageTables: Capacity,
        val slabCache: Capacity,
        val unused: Capacity
    )

    data class Disks(
        val read: Bitrate,
        val write: Bitrate,
        val readOps: Double,
        val writeOps: Double
    )

    data class Interface(
        val send: Bitrate,
        val receive: Bitrate
    )

    data class Zfs(
        val arcSize: Capacity,
        val arcMaxSize: Capacity,
        val cacheHits: Percentage
    )
}
