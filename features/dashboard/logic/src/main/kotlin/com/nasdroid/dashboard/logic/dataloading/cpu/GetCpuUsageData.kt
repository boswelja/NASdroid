package com.nasdroid.dashboard.logic.dataloading.cpu

import com.boswelja.temperature.TemperatureUnit
import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.core.strongresult.map
import com.nasdroid.dashboard.logic.dataloading.GetRealtimeStats
import com.nasdroid.dashboard.logic.dataloading.GetRealtimeStatsError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

/**
 * Retrieves CPU-related data from the server. See [invoke] for details.
 */
class GetCpuUsageData(
    private val getRealtimeStats: GetRealtimeStats,
) {

    /**
     * Returns a [Result] that contains either [CpuUsageData], or an exception if the request
     * failed.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<StrongResult<CpuUsageData, GetRealtimeStatsError>> = getRealtimeStats()
        .mapLatest {
            it.map {
                CpuUsageData(
                    utilisation = it.cpu.utilisation.toFloat(),
                    temp = it.cpu.cores.firstOrNull()?.temperature?.toLong(TemperatureUnit.CELSIUS)?.toInt() ?: 0
                )
            }
        }
}

/**
 * Describes the CPU in the system.
 *
 * @property utilisation The current CPU utilisation. The value will always be between 0 and 1.
 * @property temp The CPU temperature in celsius. This is usually measured by the hottest
 * core.
 */
data class CpuUsageData(
    val utilisation: Float,
    val temp: Int,
)
