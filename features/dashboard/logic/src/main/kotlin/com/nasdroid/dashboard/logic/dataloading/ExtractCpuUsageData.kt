package com.nasdroid.dashboard.logic.dataloading

import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.system.SystemInfo
import kotlin.math.roundToInt

/**
 * Takes data received from the server and maps it to a [DashboardData.CpuData]. See [invoke] for
 * details.
 */
@Deprecated("Switch to GetCpuUsageData")
class ExtractCpuUsageData {

    /**
     * Produces a [DashboardData.CpuData] from information retrieved from the server.
     *
     * @param systemInformation The server system information. This contains information about CPU
     * model and core count.
     * @param usageGraph The [ReportingGraphData] containing information about CPU usage.
     * @param temperatureGraph The [ReportingGraphData] containing data about CPU temperature.
     * @param uid A unique identifier for the item that will be returned.
     */
    operator fun invoke(
        systemInformation: SystemInfo,
        usageGraph: ReportingGraphData,
        temperatureGraph: ReportingGraphData,
        uid: Long,
    ): DashboardData.CpuData {
        // Usage data comes to us as a 2d array. We get the last set of values that aren't null,
        // i.e. the most recent recorded values, then take the "idle" percentage from that.
        // By subtracting the idle percentage from 100, we get the system CPU usage.
        @Suppress("MagicNumber")
        val avgUsage = (100 - usageGraph.data.last { !it.contains(null) }.last()!!) / 100.0
        // Temperature data comes to us as a 2d array. We get the last set of values that aren't
        // null, i.e. the most recent recorded values, then we take the highest number from that.
        val temp = (temperatureGraph.data.last { !it.contains(null) } as List<Double>).max().roundToInt()
        return DashboardData.CpuData(
            uid = uid,
            name = systemInformation.cpuModel,
            cores = systemInformation.physicalCores,
            threads = systemInformation.cores,
            utilisation = avgUsage.toFloat(),
            tempCelsius = temp
        )
    }
}
