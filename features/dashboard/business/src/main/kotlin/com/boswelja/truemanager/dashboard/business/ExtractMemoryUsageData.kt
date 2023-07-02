package com.boswelja.truemanager.dashboard.business

import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import com.boswelja.truemanager.core.api.v2.system.SystemInfo

class ExtractMemoryUsageData {
    operator fun invoke(
        systemInformation: SystemInfo,
        memoryGraph: ReportingGraphData
    ): DashboardData.MemoryData {
        val memoryData = memoryGraph.data.last { !it.contains(null) } as List<Double>
        return DashboardData.MemoryData(
            memoryUsed = memoryData[0].toLong().bytes,
            memoryFree = memoryData[1].toLong().bytes,
            isEcc = systemInformation.eccMemory
        )
    }
}
