package com.boswelja.truemanager.core.api.v2.pool
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class PoolV2ApiImpl(
    private val httpClient: HttpClient
) : PoolV2Api {
    override suspend fun getPools(): List<Pool> {
        val response = httpClient.get("pool")
        val dto: List<PoolDto> = response.body()
        return dto.map { it.toPool() }
    }
}

@Serializable
internal data class PoolDto(
    @SerialName("allocated")
    val allocated: Long,
    @SerialName("allocated_str")
    val allocatedStr: String,
    @SerialName("autotrim")
    val autotrim: AutotrimDto,
    @SerialName("encrypt")
    val encrypt: Int,
    @SerialName("encryptkey")
    val encryptkey: String,
    @SerialName("encryptkey_path")
    val encryptkeyPath: String?,
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
    val scan: ScanDto,
    @SerialName("size")
    val size: Long,
    @SerialName("size_str")
    val sizeStr: String,
    @SerialName("status")
    val status: String,
    @SerialName("status_detail")
    val statusDetail: String?,
    @SerialName("topology")
    val topology: TopologyDto,
    @SerialName("warning")
    val warning: Boolean
) {
    fun toPool(): Pool {
        return Pool(
            autotrim = autotrim.toAutotrim(),
            fragmentation = fragmentation.toInt(),
            freeing = freeing,
            guid = guid,
            healthy = healthy,
            id = id,
            isDecrypted = isDecrypted,
            name = name,
            path = path,
            scan = scan.toScan(),
            status = status,
            statusDetail = statusDetail,
            topology = topology.toTopology(),
            warning = warning,
            capacity = Pool.Capacity(
                allocatedBytes = allocated,
                freeBytes = free,
                sizeBytes = size
            ),
            encryption = Pool.Encryption(
                encrypt = encrypt,
                encryptkey = encryptkey,
                encryptkeyPath = encryptkeyPath,
            ),
        )
    }
    @Serializable
    internal data class AutotrimDto(
        @SerialName("parsed")
        val parsed: String,
        @SerialName("rawvalue")
        val rawvalue: String,
        @SerialName("source")
        val source: String,
        @SerialName("value")
        val value: String
    ) {
        fun toAutotrim(): Pool.Autotrim {
            return Pool.Autotrim(
                parsed = parsed == "on",
                enabled = value == "on",
                source = source
            )
        }
    }
}

@Serializable
internal data class ScanDto(
    @SerialName("bytes_issued")
    val bytesIssued: Long,
    @SerialName("bytes_processed")
    val bytesProcessed: Long,
    @SerialName("bytes_to_process")
    val bytesToProcess: Long,
    @SerialName("end_time")
    val endTime: EndTime,
    @SerialName("errors")
    val errors: Int,
    @SerialName("function")
    val function: String,
    @SerialName("pause")
    val pause: String?,
    @SerialName("percentage")
    val percentage: Double,
    @SerialName("start_time")
    val startTime: StartTime,
    @SerialName("state")
    val state: String,
    @SerialName("total_secs_left")
    val totalSecsLeft: Long?
) {
    fun toScan(): Scan {
        return Scan(
            bytesIssued = bytesIssued,
            bytesProcessed = bytesProcessed,
            bytesToProcess = bytesToProcess,
            endTime = Instant.fromEpochMilliseconds(endTime.date),
            errors = errors,
            function = function,
            pause = pause,
            percentage = percentage,
            startTime = Instant.fromEpochMilliseconds(startTime.date),
            state = state,
            remainingTime = totalSecsLeft?.seconds ?: Duration.ZERO,
        )
    }

    @Serializable
    internal data class EndTime(
        @SerialName("\$date")
        val date: Long
    )

    @Serializable
    internal data class StartTime(
        @SerialName("\$date")
        val date: Long
    )
}

@Serializable
internal data class TopologyDto(
    @SerialName("cache")
    val cache: List<DataDto>,
    @SerialName("data")
    val data: List<DataDto>,
    @SerialName("dedup")
    val dedup: List<DataDto>,
    @SerialName("log")
    val log: List<DataDto>,
    @SerialName("spare")
    val spare: List<DataDto>,
    @SerialName("special")
    val special: List<DataDto>
) {
    fun toTopology(): Topology {
        return Topology(
            cache = cache.map { it.toData() },
            data = data.map { it.toData() },
            dedup = dedup.map { it.toData() },
            log = log.map { it.toData() },
            spare = spare.map { it.toData() },
            special = special.map { it.toData() }
        )
    }
}

@Serializable
internal data class DataDto(
    @SerialName("children")
    val children: List<DataDto>,
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
    val stats: StatsDto,
    @SerialName("status")
    val status: String,
    @SerialName("type")
    val type: String,
    @SerialName("unavail_disk")
    val unavailDisk: String?
) {
    fun toData(): VDev {
        return VDev(
            children = children.map { it.toData() },
            device = device,
            disk = disk,
            guid = guid,
            name = name,
            path = path,
            stats = stats.toStats(),
            status = status,
            type = type,
            unavailDisk = unavailDisk,
        )
    }
}

@Serializable
internal data class StatsDto(
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
    val size: Int,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("write_errors")
    val writeErrors: Int
) {
    fun toStats(): Stats {
        return Stats(
            allocatedBytes = allocated,
            bytes = bytes,
            checksumErrors = checksumErrors,
            configuredAshift = configuredAshift,
            fragmentation = fragmentation,
            logicalAshift = logicalAshift,
            ops = ops,
            physicalAshift = physicalAshift,
            readErrors = readErrors,
            selfHealed = selfHealed,
            size = size,
            timestamp = Instant.fromEpochMilliseconds(timestamp),
            writeErrors = writeErrors,
        )
    }
}
