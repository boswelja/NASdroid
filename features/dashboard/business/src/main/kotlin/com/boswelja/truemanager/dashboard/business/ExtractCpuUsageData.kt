package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import com.boswelja.truemanager.core.api.v2.system.SystemInfo
import kotlin.math.roundToInt

class ExtractCpuUsageData {
    operator fun invoke(
        systemInformation: SystemInfo,
        usageGraph: ReportingGraphData,
        temperatureGraph: ReportingGraphData
    ): DashboardData.CpuData {
        @Suppress("MagicNumber")
        val avgUsage = (100 - usageGraph.data.last { !it.contains(null) }.last()!!) / 100.0
        val temp = (temperatureGraph.data.last { !it.contains(null) } as List<Double>).max().roundToInt()
        return DashboardData.CpuData(
            name = systemInformation.cpuModel,
            cores = systemInformation.physicalCores,
            threads = systemInformation.cores,
            utilisation = avgUsage.toFloat(),
            tempCelsius = temp
        )
    }
}
