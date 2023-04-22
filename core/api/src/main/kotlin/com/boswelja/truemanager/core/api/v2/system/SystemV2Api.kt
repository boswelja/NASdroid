package com.boswelja.truemanager.core.api.v2.system

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

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
enum class Environment {
    /**
     * This system is hosted on standard hardware.
     */
    DEFAULT,

    /**
     * This system is hosted on Amazon Web Services (AWS) EC2.
     */
    EC2
}

/**
 * Possible system states.
 */
enum class State {
    /**
     * The system is currently booting. It will be unusable until boot has completed.
     */
    BOOTING,

    /**
     * The system is running and ready to use.
     */
    READY,

    /**
     * The system is shutting down and is unusable.
     */
    SHUTTING_DOWN
}

/**
 * Basic system information.
 *
 * @property version The full version of the product this system is running. See [SystemV2Api.getVersion].
 * @property buildTime The time that the systems product version was built.
 * @property hostname The systems host name. It should be identifiable on the local network via this.
 * @property physicalMemoryBytes The amount of physical memory the system has, in Bytes.
 * @property cpuInfo Information about the systems CPU configuration.
 * @property uptime The amount of time the system has been running for.
 * @property license The systems license file, or null if not present.
 * @property bootTime The time this system was last booted.
 * @property birthday The systems birthday.
 * @property timezone The system time zone.
 * @property hasEccMemory Whether the server has ECC memory.
 * @property hostInfo Information about the systems host hardware.
 */
data class SystemInfo(
    val version: String,
    val buildTime: Instant,
    val hostname: String,
    val physicalMemoryBytes: Long,
    val cpuInfo: CpuInfo,
    val uptime: Duration,
    val license: String?,
    val bootTime: Instant,
    val birthday: Instant?,
    val timezone: TimeZone,
    val hasEccMemory: Boolean,
    val hostInfo: HostInfo
) {
    /**
     * Information about a CPU.
     *
     * @property model The model name of the CPU.
     * @property physicalCores The number of physical cores and threads the CPU has.
     * @property totalCores The total number of cores this CPU configuration has.
     */
    data class CpuInfo(
        val model: String,
        val physicalCores: Int,
        val totalCores: Int,
    ) {
        /**
         * The number of NUMA nodes this COU configuration has.
         */
        val numaNodes: Int = totalCores / physicalCores
    }

    /**
     * Information about host hardware for a system.
     *
     * @property serial The hardware serial number. This should match the OEM serial number.
     * @property product The name of the hardware that is running the system, for example "ProLiant DL380 Gen9".
     * @property productVersion The hardware configuration version.
     * @property manufacturer The hardware manufacturer, for example "HP".
     */
    data class HostInfo(
        val serial: String,
        val product: String,
        val productVersion: String?,
        val manufacturer: String
    )
}
