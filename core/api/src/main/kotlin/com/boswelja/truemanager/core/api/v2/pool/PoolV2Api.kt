package com.boswelja.truemanager.core.api.v2.pool

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
 * @property allocated The number of bytes in this pool that have data allocated.
 * @property allocatedStr [allocated] but a string.
 * @property autotrim [Autotrim].
 * @property encrypt TODO
 * @property encryptKey TODO
 * @property encryptKeyPath TODO
 * @property fragmentation TODO
 * @property free The number of bytes in this pool that do not have data allocated.
 * @property freeStr [free] but a string.
 * @property freeing TODO
 * @property freeingStr [freeingStr] but a string.
 * @property guid A globally unique identifier for this pool.
 * @property healthy Whether the pool is "healthy".
 * @property id The ID of the pool. This is unique to this system.
 * @property isDecrypted Whether the pool is currently decrypted.
 * @property name The name of the pool.
 * @property path The path of the pool on the filesystem.
 * @property scan The most recent [Scan] that occurred.
 * @property size The total size of the pool in bytes.
 * @property sizeStr [size] but a string.
 * @property status The pool status. For example, 'ONLINE'.
 * @property statusDetail Reasoning for the current status.
 * @property topology [Topology].
 * @property warning Whether this pool has an active warning.
 */
@Serializable
data class Pool(
    @SerialName("allocated")
    val allocated: Long,
    @SerialName("allocated_str")
    val allocatedStr: String,
    @SerialName("autotrim")
    val autotrim: Autotrim,
    @SerialName("encrypt")
    val encrypt: Int,
    @SerialName("encryptkey")
    val encryptKey: String,
    @SerialName("encryptkey_path")
    val encryptKeyPath: String?,
    @SerialName("fragmentation")
    val fragmentation: String,
    @SerialName("free")
    val free: Long,
    @SerialName("free_str")
    val freeStr: String,
    @SerialName("freeing")
    val freeing: Int,
    @SerialName("freeing_str")
    val freeingStr: String,
    @SerialName("guid")
    val guid: String,
    @SerialName("healthy")
    val healthy: Boolean,
    @SerialName("id")
    val id: Int,
    @SerialName("is_decrypted")
    val isDecrypted: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String,
    @SerialName("scan")
    val scan: Scan,
    @SerialName("size")
    val size: Long,
    @SerialName("size_str")
    val sizeStr: String,
    @SerialName("status")
    val status: String,
    @SerialName("status_detail")
    val statusDetail: String?,
    @SerialName("topology")
    val topology: Topology,
    @SerialName("warning")
    val warning: Boolean
) {
    /**
     * Describes a pools Autotrim configuration.
     *
     * @property parsed TODO
     * @property rawValue TODO
     * @property source TODO
     * @property value TODO
     */
    @Serializable
    data class Autotrim(
        @SerialName("parsed")
        val parsed: String,
        @SerialName("rawvalue")
        val rawValue: String,
        @SerialName("source")
        val source: String,
        @SerialName("value")
        val value: String
    )
}

/**
 * Describes a scan that occurred on the ZFS pool.
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
 * @property totalSecsLeft The number of seconds estimated remaining for this scan.
 */
@Serializable
data class Scan(
    @SerialName("bytes_issued")
    val bytesIssued: Long,
    @SerialName("bytes_processed")
    val bytesProcessed: Long,
    @SerialName("bytes_to_process")
    val bytesToProcess: Long,
    @SerialName("end_time")
    val endTime: Instant?,
    @SerialName("errors")
    val errors: Int,
    @SerialName("function")
    val function: String,
    @SerialName("pause")
    val pause: String?,
    @SerialName("percentage")
    val percentage: Double,
    @SerialName("start_time")
    val startTime: Instant,
    @SerialName("state")
    val state: String,
    @SerialName("total_secs_left")
    val totalSecsLeft: Long?
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
@Serializable
data class Topology(
    @SerialName("cache")
    val cache: List<VDev>,
    @SerialName("data")
    val data: List<VDev>,
    @SerialName("dedup")
    val dedup: List<VDev>,
    @SerialName("log")
    val log: List<VDev>,
    @SerialName("spare")
    val spare: List<VDev>,
    @SerialName("special")
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
@Serializable
data class VDev(
    @SerialName("children")
    val children: List<VDev>,
    @SerialName("device")
    val device: String?,
    @SerialName("disk")
    val disk: String?,
    @SerialName("guid")
    val guid: String,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String?,
    @SerialName("stats")
    val stats: Stats,
    @SerialName("status")
    val status: String,
    @SerialName("type")
    val type: String,
    @SerialName("unavail_disk")
    val unavailDisk: String?
)

/**
 * Contains various statistics for a VDev.
 *
 * @property allocated The number of bytes allocated.
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
@Serializable
data class Stats(
    @SerialName("allocated")
    val allocated: Long,
    @SerialName("bytes")
    val bytes: List<Long>,
    @SerialName("checksum_errors")
    val checksumErrors: Int,
    @SerialName("configured_ashift")
    val configuredAshift: Int,
    @SerialName("fragmentation")
    val fragmentation: Int,
    @SerialName("logical_ashift")
    val logicalAshift: Int,
    @SerialName("ops")
    val ops: List<Int>,
    @SerialName("physical_ashift")
    val physicalAshift: Int,
    @SerialName("read_errors")
    val readErrors: Int,
    @SerialName("self_healed")
    val selfHealed: Int,
    @SerialName("size")
    val size: Long,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("write_errors")
    val writeErrors: Int
)
