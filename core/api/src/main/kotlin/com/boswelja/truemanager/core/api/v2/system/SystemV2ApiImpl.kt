package com.boswelja.truemanager.core.api.v2.system

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

internal class SystemV2ApiImpl(
    private val httpClient: HttpClient
) : SystemV2Api {
    override suspend fun getBootId(): String {
        val response = httpClient.get("systen/boot_id")
        return response.body()
    }

    override suspend fun getEnvironment(): Environment {
        val response = httpClient.get("systen/boot_id")
        return when (val environmentDto: String = response.body()) {
            "DEFAULT" -> Environment.DEFAULT
            "EC2" -> Environment.EC2
            else -> error("Unknown environment '$environmentDto'")
        }
    }

    override suspend fun isFeatureEnabled(featureName: String): Boolean {
        val response = httpClient.post("systen/feature_enabled") {
            setBody(featureName)
        }
        return response.body()
    }

    override suspend fun getHostId(): String {
        val response = httpClient.get("system/host_id")
        return response.body()
    }

    override suspend fun getSystemInfo(): SystemInfo {
        val response = httpClient.get("system/info")
        val infoDto: SystemInfoDto = response.body()
        return SystemInfo(
            version = infoDto.version,
            buildTime = Instant.fromEpochMilliseconds(infoDto.buildTime.date),
            hostname = infoDto.hostName,
            physicalMemoryBytes = infoDto.physicalMemory,
            cpuInfo = SystemInfo.CpuInfo(
                model = infoDto.cpuModel,
                physicalCores = infoDto.physicalCores,
                totalCores = infoDto.cores
            ),
            uptime = infoDto.uptimeSeconds.seconds,
            license = infoDto.license,
            bootTime = Instant.fromEpochMilliseconds(infoDto.bootTime.date),
            birthday = infoDto.birthday?.let { Instant.fromEpochMilliseconds(it.date) },
            timezone = TimeZone.of(infoDto.timeZone),
            hasEccMemory = infoDto.eccMemory,
            hostInfo = SystemInfo.HostInfo(
                serial = infoDto.systemSerial,
                product = infoDto.systemProduct,
                productVersion = infoDto.systemProductVersion,
                manufacturer = infoDto.systemManufacturer
            )
        )
    }

    override suspend fun isStable(): Boolean {
        val response = httpClient.get("system/is_stable")
        return response.body()
    }

    override suspend fun updateLicense(license: String) {
        httpClient.post("systen/license_update") {
            setBody(license)
        }
    }

    override suspend fun getProductName(): String {
        val response = httpClient.get("system/product_name")
        return response.body()
    }

    override suspend fun getProductType(): String {
        val response = httpClient.get("system/product_type")
        return response.body()
    }

    override suspend fun isReady(): Boolean {
        val response = httpClient.get("system/ready")
        return response.body()
    }

    override suspend fun reboot() {
        httpClient.post("system/reboot")
    }

    override suspend fun shutdown() {
        httpClient.post("system/shutdown")
    }

    override suspend fun getState(): State {
        val response = httpClient.get("system/state")
        return when (response.body<StateDto>()) {
            StateDto.BOOTING -> State.BOOTING
            StateDto.READY -> State.READY
            StateDto.SHUTTING_DOWN -> State.SHUTTING_DOWN
        }
    }

    override suspend fun getVersion(): String {
        val response = httpClient.get("system/version")
        return response.body()
    }

    override suspend fun getShortVersion(): String {
        val response = httpClient.get("system/version_short")
        return response.body()
    }
}

@Serializable
internal enum class StateDto {
    @SerialName("BOOTING")
    BOOTING,
    @SerialName("READY")
    READY,
    @SerialName("SHUTTING_DOWN")
    SHUTTING_DOWN
}

@Serializable
internal data class SystemInfoDto(
    @SerialName("version")
    val version: String,
    @SerialName("buildtime")
    val buildTime: DateDto,
    @SerialName("hostname")
    val hostName: String,
    @SerialName("physmem")
    val physicalMemory: Long,
    @SerialName("model")
    val cpuModel: String,
    @SerialName("cores")
    val cores: Int,
    @SerialName("physical_cores")
    val physicalCores: Int,
    @SerialName("loadavg")
    val loadAvg: List<Double>,
    @SerialName("uptime")
    val uptime: String,
    @SerialName("uptime_seconds")
    val uptimeSeconds: Double,
    @SerialName("system_serial")
    val systemSerial: String,
    @SerialName("system_product")
    val systemProduct: String,
    @SerialName("system_product_version")
    val systemProductVersion: String,
    @SerialName("license")
    val license: String?,
    @SerialName("boottime")
    val bootTime: DateDto,
    @SerialName("datetime")
    val dateTime: DateDto,
    @SerialName("birthday")
    val birthday: DateDto?,
    @SerialName("timezone")
    val timeZone: String,
    @SerialName("system_manufacturer")
    val systemManufacturer: String,
    @SerialName("ecc_memory")
    val eccMemory: Boolean
) {

    @Serializable
    internal data class DateDto(
        @SerialName("\$date")
        val date: Long
    )
}
