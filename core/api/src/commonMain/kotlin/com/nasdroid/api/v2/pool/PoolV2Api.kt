package com.nasdroid.api.v2.pool

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.core.UnwrappingDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API V2 "Pool" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface PoolV2Api {

    /**
     * Get a list of [Pool]s on the system.
     *
     * @throws HttpNotOkException
     */
    suspend fun getPools(): List<Pool>

    /**
     * Creates a new pool given a [CreatePoolParams].
     *
     * @throws HttpNotOkException
     */
    suspend fun createPool(params: CreatePoolParams): Pool

    /**
     * Update pool of [id], adding the new topology.
     * The type of new data VDevs must be the same as any existing data VDevs.
     *
     * @throws HttpNotOkException
     */
    suspend fun updatePool(id: Int, params: UpdatePoolParams): Pool

    /**
     * Get details about a single [Pool].
     *
     * @throws HttpNotOkException
     */
    suspend fun getPool(id: Int): Pool

    /**
     * target_vdev is the GUID of the vdev where the disk needs to be attached. In case of STRIPED
     * vdev, this is the STRIPED disk GUID which will be converted to mirror. If target_vdev is
     * mirror, it will be converted into a n-way mirror.
     *
     * @throws HttpNotOkException
     */
    suspend fun attachDiskToPool(params: AttachPoolParams)

    /**
     * Return a list of services dependent of this pool.
     * Responsible for telling the user whether there is a related share, asking for confirmation.
     *
     * @throws HttpNotOkException
     */
    suspend fun getPoolAttachments(id: Int): List<PoolAttachment>

    /**
     * Detach a disk from a pool.
     *
     * @param id The ID of the pool to detach from.
     * @param label The VDev GUID or device name of the disk to detach.
     *
     * @throws HttpNotOkException
     */
    suspend fun detachDiskFromPool(id: Int, label: String): Boolean

    /**
     * Expand a pool to fit all available disk space.
     *
     * @throws HttpNotOkException
     */
    suspend fun expandPool(id: Int)

    /**
     * Export a pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun exportPool(id: Int, params: ExportPoolParams)

    /**
     * Returns all available datasets, except the following:
     * 1. system datasets
     * 2. application(s) internal datasets
     *
     * @throws HttpNotOkException
     */
    suspend fun getFilesystemChoices(types: List<FileSystemType> = FileSystemType.entries): List<String>

    /**
     * Get disks in use by a pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun getPoolDisks(id: Int): List<String>

    /**
     * Finds any pools that are available to be imported.
     *
     * @return An ID for running a job. The result of the job will be `List<ImportablePool>`. See
     * [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun findPoolsToImport(): Int

    /**
     * Import a pool found with [findPoolsToImport].
     *
     * @return An ID for running a job. The result of the job will be `Boolean`. See
     * [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun importPool(params: ImportPoolParams): Int

    /**
     * Returns whether or not the pool is on the latest version and with all feature flags enabled.
     *
     * @throws HttpNotOkException
     */
    suspend fun isPoolUpgraded(id: Int): Boolean

    /**
     * Offline a disk from a pool.
     *
     * @param id The ID of the pool to offline from.
     * @param label The VDev GUID or device name of the disk to offline.
     *
     * @throws HttpNotOkException
     */
    suspend fun offlineDiskFromPool(id: Int, label: String): Boolean

    /**
     * Online a disk from a pool.
     *
     * @param id The ID of the pool to online from.
     * @param label The VDev GUID or device name of the disk to online.
     *
     * @throws HttpNotOkException
     */
    suspend fun onlineDiskFromPool(id: Int, label: String): Boolean

    /**
     * Get a list of running processes using the pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun getPoolProcesses(id: Int): List<String>

    /**
     * Remove a disk from a pool.
     *
     * @param id The ID of the pool to remove from.
     * @param label The VDev GUID or device name of the disk to remove.
     *
     * @throws HttpNotOkException
     */
    suspend fun removeDiskFromPool(id: Int, label: String)

    /**
     * Replace a disk in a pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun replaceDiskInPool(id: Int, params: ReplaceDiskParams): Boolean

    /**
     * Performs a scrub action on a pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun scrubPool(id: Int, action: ScrubAction)

    /**
     * Upgrade a pool to the latest version with all feature flags.
     *
     * @throws HttpNotOkException
     */
    suspend fun upgradePool(id: Int): Boolean

    /**
     * Validates [name] is a valid name for a pool.
     *
     * @throws HttpNotOkException
     */
    suspend fun validatePoolName(name: String): Boolean
}

/**
 * Represents the parameters for replacing a disk in a pool.
 *
 * @property label The ZFS guid or a device name.
 * @property disk The identifier of the disk.
 * @property force Whether the replace operation should be forced.
 * @property preserveSettings Whether settings (power management, S.M.A.R.T., etc.) of the disk
 * being replaced should be applied to the new disk.
 */
@Serializable
data class ReplaceDiskParams(
    @SerialName("label")
    val label: String,
    @SerialName("disk")
    val disk: String,
    @SerialName("force")
    val force: Boolean = false,
    @SerialName("preserve_settings")
    val preserveSettings: Boolean = true
)
/**
 * Represents the parameters for importing a pool.
 *
 * @property guid The [ImportablePool.guid].
 * @property name The name of the pool. If not provided, it will default to the [ImportablePool.name].
 * @property enableAttachments Whether attachments that were disabled during pool export should be
 * re-enabled.
 */
@Serializable
data class ImportPoolParams(
    @SerialName("guid")
    val guid: String,
    @SerialName("name")
    val name: String? = null,
    @SerialName("enable_attachments")
    val enableAttachments: Boolean
)

/**
 * Describes a pool that is available to be imported.
 *
 * @property name The name of the pool to be imported.
 * @property guid The pools unique identifier.
 * @property status The current status of the pool.
 * @property hostname TODO
 */
@Serializable
data class ImportablePool(
    @SerialName("name")
    val name: String,
    @SerialName("guid")
    val guid: String,
    @SerialName("status")
    val status: String,
    @SerialName("hostname")
    val hostname: String,
)

/**
 * Possible pool scrub actions that can be executed. Invoke these by calling [PoolV2Api.scrubPool].
 */
@Serializable
enum class ScrubAction {
    @SerialName("START")
    Start,
    @SerialName("STOP")
    Stop,
    @SerialName("PAUSE")
    Pause
}

/**
 * Different types to retrieve file system choices for. Pass any combination of these to
 * [PoolV2Api.getFilesystemChoices] to get the possible filesystems.
 */
@Serializable
enum class FileSystemType {
    @SerialName("FILESYSTEM")
    FileSystem,
    @SerialName("VOLUME")
    Volume
}

/**
 * Represents parameters for exporting a pool.
 *
 * @property cascade will delete all attachments of the given pool.
 * @property restartServices will restart all services using the given pool.
 * @property destroy will PERMANENTLY destroy the pool and all its data.
 */
@Serializable
data class ExportPoolParams(
    @SerialName("cascade")
    val cascade : Boolean = false,
    @SerialName("restart_services")
    val restartServices: Boolean = false,
    @SerialName("destroy")
    val destroy: Boolean = false
)

/**
 * Represents a service that is attached to a pool.
 *
 * @property type The type of the service.
 * @property service The name of the service.
 * @property attachments TODO
 */
@Serializable
data class PoolAttachment(
    @SerialName("type")
    val type: String,
    @SerialName("service")
    val service: String,
    @SerialName("attachments")
    val attachments: List<String>
)

/**
 * Parameters for attaching a disk to an existing pool.
 *
 * @property oid TODO
 * @property poolAttach Describes the disk to be attached. See [PoolAttach].
 */
@Serializable
data class AttachPoolParams(
    @SerialName("oid")
    val oid: Int,
    @SerialName("pool_attach")
    val poolAttach: PoolAttach
) {
    /**
     * Describes a disk to be attached to a pool.
     *
     * @property targetVdev The GUID of the vdev to which the disk will be attached. In case of
     * STRIPED vdev, this is the STRIPED disk GUID which will be converted to mirror. If the vdev is
     * mirror, it will be converted into a n-way mirror.
     * @property newDisk The serial number of the disk to attach.
     * @property allowDuplicateSerials Whether duplicate disk serials are allowed.
     */
    @Serializable
    data class PoolAttach(
        @SerialName("target_vdev")
        val targetVdev: String,
        @SerialName("new_disk")
        val newDisk: String,
        @SerialName("allow_duplicate_serials")
        val allowDuplicateSerials: Boolean = false
    )
}

/**
 * Describes an updated topology and settings for an existing pool.
 *
 * @property topology The new topology configuration for the pool. See [NewPoolTopology].
 * @property allowDuplicateSerials Whether duplicate disk serials are allowed.
 * @property autotrim The desired state of the pool's autotrim feature. See [AutotrimMode].
 */
@Serializable
data class UpdatePoolParams(
    @SerialName("topology")
    val topology: NewPoolTopology,
    @SerialName("allow_duplicate_serials")
    val allowDuplicateSerials: Boolean = false,
    @SerialName("autotrim")
    val autotrim: AutotrimMode
) {
    /**
     * Encapsulates all possible autotrim modes.
     */
    @Serializable
    enum class AutotrimMode {
        @SerialName("ON")
        On,
        @SerialName("OFF")
        Off
    }
}

/**
 * Describes a new pool to be created.
 *
 * @property name The name of the pool. You can validate this name before trying to create by
 * invoking [PoolV2Api.validatePoolName].
 * @property encryption Whether the root dataset of the new pool should be encrypted.
 * @property deduplication The mode for file deduplication on the root dataset of the new pool, or
 * null if deduplication should not be enabled. See [DeduplicationMode] for options.
 * @property checksum TODO
 * @property encryptionOptions The encryption options if you are enabling encryption. See
 * [EncryptionOptions].
 * @property topology Describes the topology of the new pool. See [NewPoolTopology].
 * @property allowDuplicateSerials Whether disks with identical serial numbers are allowed in the
 * pool.
 */
@Serializable
data class CreatePoolParams(
    @SerialName("name")
    val name: String,
    @SerialName("encryption")
    val encryption: Boolean = false,
    @SerialName("deduplication")
    val deduplication: DeduplicationMode? = null,
    @SerialName("checksum")
    val checksum: Checksum? = null,
    @SerialName("encryption_options")
    val encryptionOptions: EncryptionOptions? = null,
    @SerialName("topology")
    val topology: NewPoolTopology,
    @SerialName("allow_duplicate_serials")
    val allowDuplicateSerials: Boolean = false
) {

    /**
     * Encapsulates all possible file deduplication modes for a pool.
     */
    @Serializable
    enum class DeduplicationMode {
        /**
         * Ensures that no block of data is duplicated in the pool.
         */
        @SerialName("ON")
        On,

        /**
         * Similar to [On], but byte-level comparison is performed. This is expensive, and should
         * only be used in special circumstances.
         */
        @SerialName("VERIFY")
        Verify,

        /**
         * No deduplication is performed.
         */
        @SerialName("OFF")
        Off
    }

    /**
     * TODO What is this?
     */
    @Serializable
    enum class Checksum {
        @SerialName("ON")
        On,
        @SerialName("OFF")
        Off,
        @SerialName("FLETCHER2")
        Fletcher2,
        @SerialName("FLETCHER4")
        Fletcher4,
        @SerialName("SHA256")
        SHA256,
        @SerialName("SHA512")
        SHA512,
        @SerialName("SKEIN")
        Skein,
        @SerialName("EDONR")
        Edonr,
        @SerialName("BLAKE3")
        Blake3
    }

    /**
     * Describes configuration for encryption of root dataset for the new pool. [passphrase] must be
     * specified if encryption for root dataset is desired with a passphrase as a key. Otherwise a
     * hex encoded key can be specified by providing [key]. [generateKey] when enabled
     * automatically generates the key to be used for dataset encryption.
     *
     * @property generateKey Whether the encryption key should be automatically generated.
     * @property pbkdf2iters TODO
     * @property algorithm The encryption algorithm to use.
     * @property passphrase The passphrase used for the encryption, if encrypting with a passphrase.
     * @property key The hexadecimal key used for encryption, in encrypting with a key.
     */
    @Serializable
    data class EncryptionOptions(
        @SerialName("generate_key")
        val generateKey: Boolean = false,
        @SerialName("pbkdf2iters")
        val pbkdf2iters: Int = 350000,
        @SerialName("algorithm")
        val algorithm: Algorithm = Algorithm.AES_256_GCM,
        @SerialName("passphrase")
        val passphrase: String? = null,
        @SerialName("key")
        val key: String? = null
    ) {

        /**
         * Encapsulates all possible encryption algorithms for the root dataset of the new pool.
         */
        @Serializable
        enum class Algorithm {
            @SerialName("AES-128-CCM")
            AES_128_CCM,
            @SerialName("AES-192-CCM")
            AES_192_CCM,
            @SerialName("AES-256-CCM")
            AES_256_CCM,
            @SerialName("AES-128-GCM")
            AES_128_GCM,
            @SerialName("AES-192-GCM")
            AES_192_GCM,
            @SerialName("AES-256-GCM")
            AES_256_GCM,
        }
    }
}

/**
 * Describes the topology of a new or updated pool.
 *
 * @property data A list of [DataVDev]s to be added to the pool. Data vdevs are used for primary
 * storage operations. You must have at least one for any pool.
 * @property special An optional list of [SpecialVDev]s to be added to the pool. These vdevs are
 * used to speed up metadata and small block IO.
 * @property dedup An optional list of [DedupVDev]s to be added to the pool. Dedup vdevs are used to
 * store deduplication tables. These vdevs must be sized x GiB for each X TiB of [data].
 * @property cache An optional list of [CacheVDev]s to be added to the pool. Cache vdevs hold the
 * ZFS L2ARC read-cache to be used with fast devices for accelerated read times.
 * @property log An optional list of [LogVDev]s to be added to the pool. Log vdevs hold a ZFS LOG
 * that can improve the speed of synchronous write operations.
 * @property spares An optional list of drives that are reserved to replace drives in [data] when
 * they fail.
 */
@Serializable
data class NewPoolTopology(
    @SerialName("data")
    val data: List<DataVDev>,
    @SerialName("special")
    val special: List<SpecialVDev> = emptyList(),
    @SerialName("dedup")
    val dedup: List<DedupVDev> = emptyList(),
    @SerialName("cache")
    val cache: List<CacheVDev> = emptyList(),
    @SerialName("log")
    val log: List<LogVDev> = emptyList(),
    @SerialName("spares")
    val spares: List<String> = listOf()
) {
    /**
     * Describes a single vdev used for primary storage operations.
     *
     * @property type The type, or layout, of the vdev. See [Type] for options.
     * @property disks A list of disks in the vdev.
     * @property draidDataDisks TODO
     * @property draidSpareDisks TODO
     */
    @Serializable
    data class DataVDev(
        @SerialName("type")
        val type: Type,
        @SerialName("disks")
        val disks: List<String>,
        @SerialName("draid_data_disks")
        val draidDataDisks: Int? = null,
        @SerialName("draid_spare_disks")
        val draidSpareDisks: Int? = null
    ) {
        /**
         * Encapsulates all possible data vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("DRAID1")
            DRAID1,
            @SerialName("DRAID2")
            DRAID2,
            @SerialName("DRAID3")
            DRAID3,
            @SerialName("RAIDZ1")
            RAIDZ1,
            @SerialName("RAIDZ2")
            RAIDZ2,
            @SerialName("RAIDZ3")
            RAIDZ3,
            @SerialName("MIRROR")
            MIRROR,
            @SerialName("STRIPE")
            STRIPE
        }
    }

    /**
     * Describes a single vdev used for metadata and small block IO.
     *
     * @property type The type, or layout, of the vdev. See [Type] for options.
     * @property disks A list of disks in the vdev.
     */
    @Serializable
    data class SpecialVDev(
        @SerialName("type")
        val type: Type,
        @SerialName("disks")
        val disks: List<String>
    ) {
        /**
         * Encapsulates all possible special vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("MIRROR")
            MIRROR,
            @SerialName("STRIPE")
            STRIPE
        }
    }

    /**
     * Describes a single vdev used for deduplication tables.
     *
     * @property type The type, or layout, of the vdev. See [Type] for options.
     * @property disks A list of disks in the vdev.
     */
    @Serializable
    data class DedupVDev(
        @SerialName("type")
        val type: Type,
        @SerialName("disks")
        val disks: List<String>
    ) {
        /**
         * Encapsulates all possible dedup vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("MIRROR")
            MIRROR,
            @SerialName("STRIPE")
            STRIPE
        }
    }

    /**
     * Describes a single vdev used for ZFS L2ARC cache.
     *
     * @property type The type, or layout, of the vdev. See [Type] for options.
     * @property disks A list of disks in the vdev.
     */
    @Serializable
    data class CacheVDev(
        @SerialName("type")
        val type: Type,
        @SerialName("disks")
        val disks: List<String>
    ) {
        /**
         * Encapsulates all possible cache vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("STRIPE")
            STRIPE
        }
    }

    /**
     * Describes a single vdev used for ZFS LOG.
     *
     * @property type The type, or layout, of the vdev. See [Type] for options.
     * @property disks A list of disks in the vdev.
     */
    @Serializable
    data class LogVDev(
        @SerialName("type")
        val type: Type,
        @SerialName("disks")
        val disks: List<String>
    ) {
        /**
         * Encapsulates all possible log vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("MIRROR")
            MIRROR,
            @SerialName("STRIPE")
            STRIPE
        }
    }

}

/**
 * Describes a pool of disks on the system.
 *
 * @property id The ID of the pool. This is unique to this system.
 * @property name The name of the pool.
 * @property guid A globally unique identifier for this pool.
 * @property status The overall status of the pool. See [PoolStatus] for possible modes.
 * @property path The path of the pool on the filesystem.
 * @property scan The most recent [Scan] that occurred.
 * @property isUpgraded Whether the pool is on the latest version with all feature flags enabled.
 * @property healthy Whether the pool is "healthy".
 * @property warning Whether this pool has an active warning.
 * @property statusCode The code for the current pool status, if issues are present.
 * @property statusDetail Reasoning for the current status.
 * @property size The total size of the pool in bytes.
 * @property allocated The number of bytes in this pool that have data allocated.
 * @property free The number of bytes in this pool that do not have data allocated.
 * @property freeing TODO
 * @property fragmentation TODO
 * @property sizeStr [size] but a string.
 * @property allocatedStr [allocated] but a string.
 * @property freeStr [free] but a string.
 * @property freeingStr [freeingStr] but a string.
 * @property autotrim [Autotrim].
 * @property topology [Topology].
 */
@Serializable
data class Pool(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("guid")
    val guid: String,
    @SerialName("status")
    val status: PoolStatus,
    @SerialName("path")
    val path: String,
    @SerialName("scan")
    val scan: Scan,
    @SerialName("is_upgraded")
    val isUpgraded: Boolean?,
    @SerialName("healthy")
    val healthy: Boolean,
    @SerialName("warning")
    val warning: Boolean,
    @SerialName("status_code")
    val statusCode: String?,
    @SerialName("status_detail")
    val statusDetail: String?,
    @SerialName("size")
    val size: Long?,
    @SerialName("allocated")
    val allocated: Long?,
    @SerialName("free")
    val free: Long?,
    @SerialName("freeing")
    val freeing: Long?,
    @SerialName("fragmentation")
    val fragmentation: String?,
    @SerialName("size_str")
    val sizeStr: String?,
    @SerialName("allocated_str")
    val allocatedStr: String?,
    @SerialName("free_str")
    val freeStr: String?,
    @SerialName("freeing_str")
    val freeingStr: String?,
    @SerialName("autotrim")
    val autotrim: Autotrim,
    @SerialName("topology")
    val topology: Topology,
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

    /**
     * Describes a scan that occurred on the ZFS pool.
     *
     * @property bytesIssued The number of bytes with issues.
     * @property bytesProcessed The number of bytes processed in the scan.
     * @property bytesToProcess total number of bytes to process for this scan.
     * @property endTime The time this scan finished.
     * @property errors The number of errors found in the scan.
     * @property function The scan function. For example, 'SCRUB'.
     * @property pause The time this scan was paused, if it is currently paused.
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
        @Serializable(with = UnwrappingDateSerializer::class)
        @SerialName("end_time")
        val endTime: Long?,
        @SerialName("errors")
        val errors: Int,
        @SerialName("function")
        val function: String,
        @Serializable(with = UnwrappingDateSerializer::class)
        @SerialName("pause")
        val pause: Long?,
        @SerialName("percentage")
        val percentage: Double,
        @Serializable(with = UnwrappingDateSerializer::class)
        @SerialName("start_time")
        val startTime: Long,
        @SerialName("state")
        val state: ScanState,
        @SerialName("total_secs_left")
        val totalSecsLeft: Long?
    ) {

        /**
         * Encapsulates all possible states for a scan on a pool.
         */
        @Serializable
        enum class ScanState {
            @SerialName("FINISHED")
            Finished,
            @SerialName("SCANNING")
            Scanning,
            @SerialName("CANCELED")
            Cancelled,
        }
    }

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
        val status: PoolStatus,
        @SerialName("type")
        val type: Type,
        @SerialName("unavail_disk")
        val unavailDisk: String?
    ) {

        /**
         * Encapsulates all possible vdev types, or layouts.
         */
        @Serializable
        enum class Type {
            @SerialName("DRAID1")
            DRaid1,
            @SerialName("DRAID2")
            DRaid2,
            @SerialName("DRAID3")
            DRaid3,
            @SerialName("RAIDZ1")
            RaidZ1,
            @SerialName("RAIDZ2")
            RaidZ2,
            @SerialName("RAIDZ3")
            RaidZ3,
            @SerialName("MIRROR")
            Mirror,
            @SerialName("STRIPE")
            Stripe,
            @SerialName("DISK")
            Disk,
        }
    }

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
}

/**
 * Encapsulates all possible statuses of a vdev.
 */
@Serializable
enum class PoolStatus {
    @SerialName("ONLINE")
    Online,
    @SerialName("DEGRADED")
    Degraded,
    @SerialName("OFFLINE")
    Offline
}
