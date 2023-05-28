package com.boswelja.truemanager.dashboard.ui.overview

import com.boswelja.capacity.Capacity
import com.boswelja.capacity.CapacityUnit
import kotlinx.datetime.LocalDateTime

sealed interface DashboardData {

    /**
     * Describes the CPU in the system.
     *
     * @property name The name of the CPU. E.g. "Intel(R) Xeon(R) CPU E5-2680".
     * @property cores The total number of cores the CPU has.
     * @property threads The total number of threads the CPU has.
     * @property tempCelsius The CPU temperature, in celsius. This is usually measured by the hottest
     * core.
     * @property avgUsage The average CPU utilisation. The value will always be between 0 and 1.
     */
    data class CpuData(
        val name: String,
        val cores: Int,
        val threads: Int,
        val avgUsage: Float,
        val tempCelsius: Int,
    ) : DashboardData

    /**
     * Describes the memory in the system.
     *
     * @property memoryUsed The amount of memory currently in use.
     * @property memoryFree The amount of memory currently free.
     * @property isEcc Whether the memory supports ECC.
     */
    data class MemoryData(
        val memoryUsed: Capacity,
        val memoryFree: Capacity,
        val isEcc: Boolean
    ) : DashboardData {
        val totalMemory = memoryFree + memoryUsed
        val usedPercent = memoryUsed.toLong(CapacityUnit.BYTE) / totalMemory.toLong(CapacityUnit.BYTE).toFloat()
    }

    data class NetworkUsageData(
        val adaptersData: List<AdapterData>
    ) : DashboardData {

        /**
         * Describes the state of a network adapter.
         *
         * @property name The name of the adapter.
         * @property address The IP address of the adapter on the network.
         * @property receivedBytes A list of incoming bytes over the specified period, where index 0
         * occurred at the start of [period] and last index occurred at the end of [period].
         * @property sentBytes A list of outgoing bytes over the specified period, where index 0
         * occurred at the start of [period] and last index occurred at the end of [period].
         * @property period A range of [LocalDateTime]s that the data occurred over.
         */
        data class AdapterData(
            val name: String,
            val address: String,
            val receivedBytes: List<Double>,
            val sentBytes: List<Double>,
            val period: ClosedRange<LocalDateTime>
        )
    }

    /**
     * Contains basic information about a TrueNAS system.
     *
     * @property version The full version the system is running.
     * @property hostname The system hostname. This helps identify the system on the network.
     * @property lastBootTime The time the system was last started. This is used to calculate uptime.
     */
    data class SystemInformationData(
        val version: String,
        val hostname: String,
        val lastBootTime: LocalDateTime
    ) : DashboardData
}
