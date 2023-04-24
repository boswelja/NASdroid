package com.boswelja.truemanager.core.api.v2.pool

import kotlinx.datetime.Instant
import kotlin.time.Duration

/**
 * Describes the TrueNAS API V2 "Pool" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface PoolV2Api {

    /**
     * Get a list of [Pool]s on the system.
     */
    suspend fun getPools(): List<Pool>
}

/**
 * Describes a pool of disks on the system.
 *
 * @property autotrim [Autotrim].
 * @property fragmentation TODO
 * @property freeing TODO
 * @property guid A globally unique identifier for this pool.
 * @property healthy Whether the pool is "healthy".
 * @property id The ID of the pool. This is unique to this system.
 * @property isDecrypted Whether the pool is currently decrypted.
 * @property name The name of the pool.
 * @property path The path of the pool on the filesystem.
 * @property scan The most recent [Scan] that occurred.
 * @property status The pool status. For example, 'ONLINE'.
 * @property statusDetail Reasoning for the current status.
 * @property topology [Topology].
 * @property warning Whether this pool has an active warning.
 * @property capacity [Capacity].
 * @property encryption [Encryption].
 */
data class Pool(
    val autotrim: Autotrim,
    val fragmentation: Int,
    val freeing: Int,
    val guid: String,
    val healthy: Boolean,
    val id: Int,
    val isDecrypted: Boolean,
    val name: String,
    val path: String,
    val scan: Scan,
    val status: String,
    val statusDetail: String?,
    val topology: Topology,
    val warning: Boolean,
    val capacity: Capacity,
    val encryption: Encryption
) {
    /**
     * Describes a pools Autotrim configuration.
     *
     * @property parsed TODO
     * @property source TODO
     * @property enabled Whether Autotrim is enabled.
     */
    data class Autotrim(
        val parsed: Boolean,
        val source: String,
        val enabled: Boolean
    )

    /**
     * Describes the capacity and data allocation of a pool.
     *
     * @property allocatedBytes The number of bytes allocated in the pool
     * @property freeBytes The number of bytes that are free in the pool.
     * @property sizeBytes The total size of the pool in bytes.
     */
    data class Capacity(
        val allocatedBytes: Long,
        val freeBytes: Long,
        val sizeBytes: Long
    )

    /**
     * Describes the pools encryption status.
     *
     * @property encrypt TODO
     * @property encryptkey TODO
     * @property encryptkeyPath TODO
     */
    data class Encryption(
        val encrypt: Int,
        val encryptkey: String,
        val encryptkeyPath: String?,
    )
}

/**
 * Describes a scan that occurred on a pool.
 *
 * @property bytesIssued The number of bytes with issues.
 * @property bytesProcessed The number of bytes processed in the scan.
 * @property bytesToProcess total number of bytes to process for this scan.
 * @property endTime The time this scan finished.
 * @property errors The number of errors found in the scan.
 * @property function The scan function. For example, 'SCRUB'.
 * @property pause TODO
 * @property percentage How much of the scan has been completed.
 * @property startTime The time this scan started.
 * @property state The state of the scan. For example, 'FINISHED'.
 * @property remainingTime The estimated remaining time for this scan.
 */
data class Scan(
    val bytesIssued: Long,
    val bytesProcessed: Long,
    val bytesToProcess: Long,
    val endTime: Instant,
    val errors: Int,
    val function: String,
    val pause: String?,
    val percentage: Double,
    val startTime: Instant,
    val state: String,
    val remainingTime: Duration
)

/**
 * Describes a pool topology.
 *
 * @property cache A list of [VDev] that are used for caching.
 * @property data A list of [VDev] that are used for data storage.
 * @property dedup A list of [VDev] that are used for deduplication.
 * @property log A list of [VDev] that are used for storing logs.
 * @property spare A list of [VDev] that are used as hot spares.
 * @property special A list of "special" [VDev].
 */
data class Topology(
    val cache: List<VDev>,
    val data: List<VDev>,
    val dedup: List<VDev>,
    val log: List<VDev>,
    val spare: List<VDev>,
    val special: List<VDev>
)

/**
 * Describes a VDev in a pool.
 *
 * @property children A list of child [VDev]s.
 * @property device The name of the device. For example, `sda1`.
 * @property disk The name of the disk. For example, `sda`.
 * @property guid a Globally unique identifier for this VDev.
 * @property name The name of this VDev.
 * @property path The path of this VDev.
 * @property stats [Stats].
 * @property status The status of the VDev.
 * @property type The VDev type.
 * @property unavailDisk TODO
 */
data class VDev(
    val children: List<VDev>,
    val device: String?,
    val disk: String?,
    val guid: String,
    val name: String,
    val path: String?,
    val stats: Stats,
    val status: String,
    val type: String,
    val unavailDisk: String?
)

/**
 * Contains various statistics for a VDev.
 *
 * @property allocatedBytes The number of bytes allocated.
 * @property bytes TODO
 * @property checksumErrors The number of checksum errors.
 * @property configuredAshift TODO
 * @property fragmentation TODO
 * @property logicalAshift TODO
 * @property ops TODO
 * @property physicalAshift TODO
 * @property readErrors The number of read errors that have occurred.
 * @property selfHealed The number of self-healed sectors.
 * @property size TODO
 * @property timestamp TODO
 * @property writeErrors The number of write errors that have occurred.
 */
data class Stats(
    val allocatedBytes: Long,
    val bytes: List<Long>,
    val checksumErrors: Int,
    val configuredAshift: Int,
    val fragmentation: Int,
    val logicalAshift: Int,
    val ops: List<Int>,
    val physicalAshift: Int,
    val readErrors: Int,
    val selfHealed: Int,
    val size: Int,
    val timestamp: Instant,
    val writeErrors: Int
)
