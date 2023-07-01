package com.boswelja.truemanager.core.api.v2.system

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API V2 "System" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
@Suppress("TooManyFunctions")
interface SystemV2Api {

    /**
     * Get a unique identifier that will be reset on each system reboot.
     */
    suspend fun getBootId(): String

    /**
     * Get the environment in which product is running.
     */
    suspend fun getEnvironment(): Environment

    /**
     * Get whether the given feature is enabled.
     */
    suspend fun isFeatureEnabled(featureName: String): Boolean

    /**
     * Get a hex string that is generated based on the contents of the /etc/hostid file. This is a
     * permanent value that persists across reboots/upgrades and can be used as a unique identifier
     * for the machine.
     */
    suspend fun getHostId(): String

    /**
     * Get basic system information.
     */
    suspend fun getSystemInfo(): SystemInfo

    /**
     * Get whether the system is currently running a stable build.
     */
    suspend fun isStable(): Boolean

    /**
     * Update license file.
     */
    suspend fun updateLicense(license: String)

    /**
     * Get the name of the product the system is using. FOr example, "TrueNAS".
     */
    suspend fun getProductName(): String

    /**
     * Get the type of product the system is using. For example, "SCALE"
     */
    suspend fun getProductType(): String

    /**
     * Get whether the system is booted and ready to use.
     */
    suspend fun isReady(): Boolean

    /**
     * Reboot the system.
     */
    suspend fun reboot()

    /**
     * Shutdown the system.
     */
    suspend fun shutdown()

    /**
     * Get the current system state.
     */
    suspend fun getState(): State

    /**
     * Get the version of the product this system is using. For example, "TrueNAS-SCALE-22.12.2".
     */
    suspend fun getVersion(): String

    /**
     * Get a shortened version of the product this system is using. For example, "22.12.2".
     */
    suspend fun getShortVersion(): String
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
 * @property systemSerial TODO
 * @property systemProduct TODO
 * @property systemProductVersion TODO
 * @property license The systems license file, or null if not present.
 * @property bootTime The time this system was last booted.
 * @property dateTime TODO
 * @property birthday The systems birthday.
 * @property timeZone The time zone the system is configured to use.
 * @property systemManufacturer The company that produced the system.
 * @property eccMemory Whether the server has ECC memory.
 */
data class SystemInfo(
    @SerialName("version")
    val version: String,
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
    val license: String?,
    @SerialName("boottime")
    val bootTime: Instant,
    @SerialName("datetime")
    val dateTime: Instant,
    @SerialName("birthday")
    val birthday: Instant?,
    @SerialName("timezone")
    val timeZone: String,
    @SerialName("system_manufacturer")
    val systemManufacturer: String,
    @SerialName("ecc_memory")
    val eccMemory: Boolean
)
