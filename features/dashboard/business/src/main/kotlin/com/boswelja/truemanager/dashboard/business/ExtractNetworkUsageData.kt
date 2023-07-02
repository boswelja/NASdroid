package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ExtractNetworkUsageData {

    operator fun invoke(adapterGraphs: List<ReportingGraphData>): DashboardData.NetworkUsageData {
        val adaptersInfo = adapterGraphs
            .filter { graph -> graph.data.any { line -> line.any { point -> point != null && point > 0 } } }
            .map { graph ->
                val data = graph.data.filter { !it.contains(null) } as List<List<Double>>
                val start = Instant.fromEpochMilliseconds(graph.start).toLocalDateTime(TimeZone.currentSystemDefault())
                val end = Instant.fromEpochMilliseconds(graph.end).toLocalDateTime(TimeZone.currentSystemDefault())
                DashboardData.NetworkUsageData.AdapterData(
                    name = graph.identifier!!,
                    address = "TODO",
                    receivedBytes = data.map { it[0] },
                    sentBytes = data.map { it[1] },
                    period = start..end
                )
            }
        return DashboardData.NetworkUsageData(adaptersInfo)
    }
}
