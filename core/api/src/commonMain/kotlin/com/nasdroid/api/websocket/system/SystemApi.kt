package com.nasdroid.api.websocket.system

import com.nasdroid.api.TimestampUnwrapper
import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.websocket.ddp.EDateInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Describes the TrueNAS API V2 "System" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
@Suppress("TooManyFunctions")
interface SystemApi {

    /**
     * Get a unique identifier that will be reset on each system reboot.
     */
    suspend fun bootId(): String

    /**
     * Retrieve build time of the system.
     */
    suspend fun buildTime(): Instant

    /**
     * Download a debug file. Returns a job ID.
     */
    suspend fun debug(): Int

    /**
     * Get whether the given feature is enabled.
     */
    suspend fun featureEnabled(feature: Feature): Boolean

    /**
     * Get a hex string that is generated based on the contents of the /etc/hostid file. This is a
     * permanent value that persists across reboots/upgrades and can be used as a unique identifier
     * for the machine.
     */
    suspend fun hostId(): String

    /**
     * Get basic system information.
     */
    suspend fun info(): SystemInfo

    /**
     * Get whether the system is currently running a stable build.
     */
    suspend fun isStable(): String

    /**
     * Update license file.
     */
    suspend fun licenseUpdate(license: String)

    /**
     * Get the type of product the system is using. For example, "SCALE".
     */
    suspend fun productType(): ProductType

    /**
     * Get whether the system is booted and ready to use.
     */
    suspend fun ready(): Boolean

    /**
     * Reboot the system.
     */
    suspend fun reboot(delay: Duration): Int

    /**
     * Returns the release notes URL for a version of SCALE.
     * If version is not provided, then the release notes URL will return a link for the currently
     * installed version of SCALE.
     *
     * @param version represents a version to check against
     */
    suspend fun releaseNotesUrl(version: String? = null): String?

    /**
     * Shutdown the system.
     *
     * @throws HttpNotOkException
     */
    suspend fun shutdown(delay: Duration): Int

    /**
     * Get the current system state.
     */
    suspend fun state(): State

    /**
     * Get the version of the product this system is using. For example, "TrueNAS-SCALE-22.12.2".
     */
    suspend fun version(): String

    /**
     * Get a shortened version of the product this system is using. For example, "22.12.2".
     */
    suspend fun versionShort(): String
}

@Serializable
enum class Feature {
    @SerialName("DEDUP")
    Dedup,
    @SerialName("FIBRECHANNEL")
    FibreChannel,
    @SerialName("VM")
    VM
}

/**
 * Possible host environment types for a system.
 */
@Serializable
enum class Environment {
    /**
     * This system is hosted on standard hardware.
     */
    @SerialName("DEFAULT")
    DEFAULT,

    /**
     * This system is hosted on Amazon Web Services (AWS) EC2.
     */
    @SerialName("EC2")
    EC2
}

/**
 * Possible system states.
 */
@Serializable
enum class State {
    /**
     * The system is currently booting. It will be unusable until boot has completed.
     */
    @SerialName("BOOTING")
    BOOTING,

    /**
     * The system is running and ready to use.
     */
    @SerialName("READY")
    READY,

    /**
     * The system is shutting down and is unusable.
     */
    @SerialName("SHUTTING_DOWN")
    SHUTTING_DOWN
}

/**
 * All possible product types. See [SystemApi.productType] to get the value from the server.
 */
@Serializable
enum class ProductType {
    /**
     * TrueNAS SCALE, community version.
     */
    @SerialName("SCALE")
    Scale,

    /**
     * TrueNAS SCALE Enterprise, appliance version.
     */
    @SerialName("SCALE_ENTERPRISE")
    ScaleEnterprise
}

/**
 * Basic system information.
 *
 * @property version The full version of the product this system is running. See [SystemV2Api.getVersion].
 * @property buildTime The time that the systems product version was built.
 * @property hostName The systems host name. It should be identifiable on the local network via this.
 * @property physicalMemory The amount of physical memory the system has, in Bytes.
 * @property cpuModel The model name of the CPU this system has.
 * @property cores The total number of cores and threads the system has.
 * @property physicalCores The number of physical cores the system has.
 * @property loadAvg TODO
 * @property uptime The amount of time the system has been running for.
 * @property uptimeSeconds The number of seconds the system has been up for.
 * @property systemSerial The serial number of the system, as specified by the OEM.
 * @property systemProduct The system product name, as specified by the OEM.
 * @property systemProductVersion TODO
 * @property license The systems license file, or null if not present.
 * @property bootTime The time this system was last booted.
 * @property dateTime The current time, according to the system.
 * @property timeZone The time zone the system is configured to use.
 * @property systemManufacturer The company that produced the system.
 * @property eccMemory Whether the server has ECC memory.
 */
@Serializable
data class SystemInfo(
    @SerialName("version")
    val version: String,
    @Serializable(EDateInstantSerializer::class)
    @SerialName("buildtime")
    val buildTime: Instant,
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
    val license: String?, // TODO Should be an object?
    @Serializable(EDateInstantSerializer::class)
    @SerialName("boottime")
    val bootTime: Instant,
    @Serializable(EDateInstantSerializer::class)
    @SerialName("datetime")
    val dateTime: Instant,
    @SerialName("timezone")
    val timeZone: TimeZone,
    @SerialName("system_manufacturer")
    val systemManufacturer: String,
    @SerialName("ecc_memory")
    val eccMemory: Boolean
)
