package com.boswelja.truemanager.dashboard.logic

import com.boswelja.capacity.Capacity
import com.boswelja.capacity.CapacityUnit
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.dashboard.logic.DashboardData.NetworkUsageData.AdapterData
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Flows a list of [DashboardData], accounting for the users preferred item order and visibility.
 * See [invoke] for details.
 */
class GetDashboardData(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api,
    private val getReportingDataForEntries: GetReportingDataForEntries,
    private val extractDashboardData: ExtractDashboardData,
) {

    /**
     * Flow a List of [DashboardData], where the number of list items will be reconfigured based on
     * the users preferred entry order and visibility. Data will refresh automatically at 10 second
     * intervals.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<DashboardData>> = flow {
        val serverId = systemV2Api.getHostId()
        val flow = configuration.getVisibleEntries(serverId)
            .flatMapLatest { entries ->
                repeatingFlow(10.seconds) {
                    val graphs = getReportingDataForEntries(entries)
                    val systemInformation = systemV2Api.getSystemInfo()

                    extractDashboardData(entries, graphs, systemInformation)
                }
            }

        emitAll(flow)
    }

    @OptIn(ExperimentalTime::class)
    private fun <T> repeatingFlow(interval: Duration, producer: suspend () -> T): Flow<T> = flow {
        while (coroutineContext.isActive) {
            val callTime = measureTime {
                emit(producer())
            }
            delay((interval - callTime).coerceAtLeast(Duration.ZERO))
        }
    }
}

/**
 * Describes some data that should be displayed in the dashboard.
 */
sealed interface DashboardData {

    /**
     * Describes the CPU in the system.
     *
     * @property name The name of the CPU. E.g. "Intel(R) Xeon(R) CPU E5-2680".
     * @property cores The total number of cores the CPU has.
     * @property threads The total number of threads the CPU has.
     * @property utilisation The current CPU utilisation. The value will always be between 0 and 1.
     * @property tempCelsius The CPU temperature, in celsius. This is usually measured by the hottest
     * core.
     */
    data class CpuData(
        val name: String,
        val cores: Int,
        val threads: Int,
        val utilisation: Float,
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
        /**
         * The total amount of memory installed in the system.
         */
        val totalMemory = memoryFree + memoryUsed

        /**
         * The percentage value of used memory, between 0 and 1.
         */
        val usedPercent = memoryUsed.toLong(CapacityUnit.BYTE) / totalMemory.toLong(CapacityUnit.BYTE).toFloat()
    }

    /**
     * Describes multiple network adapters, which should be grouped in the dashboard.
     *
     * @property adaptersData A list of [AdapterData] describing a single adapter.
     */
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
