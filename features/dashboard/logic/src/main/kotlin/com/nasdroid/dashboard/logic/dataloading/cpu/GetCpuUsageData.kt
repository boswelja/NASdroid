package com.nasdroid.dashboard.logic.dataloading.cpu

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlin.coroutines.coroutineContext
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

/**
 * Retrieves CPU-related data from the server. See [invoke] for details.
 */
class GetCpuUsageData(
    private val reportingV2Api: ReportingV2Api,
) {
    suspend operator fun invoke(): Result<CpuUsageData> = runCatching {
        val now = Clock.System.now()
        val (usageGraph, temperatureGraph) = reportingV2Api.getGraphData(
            graphs = listOf(
                RequestedGraph(CPU_GRAPH_NAME, null),
                RequestedGraph(CPU_TEMP_GRAPH_NAME, null)
            ),
            start = now - 20.seconds,
            end = now,
        )

        // Usage data comes to us as a 2d array. We get the last set of values that aren't null,
        // i.e. the most recent recorded values, then take the "idle" percentage from that.
        // By subtracting the idle percentage from 100, we get the system CPU usage.
        val idleIndex = usageGraph.legend.indexOf("idle")
        @Suppress("MagicNumber")
        val avgUsage = (100 - usageGraph.data.last { !it.contains(null) }[idleIndex]!!) / 100.0
        // Temperature data comes to us as a 2d array. We get the last set of values that aren't
        // null, i.e. the most recent recorded values, then we take the highest number from that.
        val temp = (temperatureGraph.data.last { !it.contains(null) } as List<Double>)
            .reduceRightIndexed { index, d, acc ->
                // If the item we are looking at isn't directly associated with a core (or thread), we ignore it.
                if (temperatureGraph.legend[index].toIntOrNull() != null) {
                    // We "accumulate" the highest value to get the highest temp
                    if (acc > d) {
                        acc
                    } else {
                        d
                    }
                } else {
                    acc
                }
            }
            .roundToInt()
        CpuUsageData(
            utilisation = avgUsage.toFloat(),
            temp = temp
        )
    }

    private fun <T> repeatingFlow(interval: Duration, producer: suspend () -> T): Flow<T> = flow {
        while (coroutineContext.isActive) {
            val callTime = measureTime {
                emit(producer())
            }
            delay((interval - callTime).coerceAtLeast(Duration.ZERO))
        }
    }

    companion object {
        private const val CPU_GRAPH_NAME = "cpu"
        private const val CPU_TEMP_GRAPH_NAME = "cputemp"
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
