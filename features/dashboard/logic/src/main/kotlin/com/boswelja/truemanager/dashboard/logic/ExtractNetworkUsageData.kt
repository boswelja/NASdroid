package com.boswelja.truemanager.dashboard.logic

import com.boswelja.truemanager.core.api.v2.reporting.ReportingGraphData
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Takes data received from the server and maps it to a [DashboardData.NetworkUsageData]. See
 * [invoke] for details.
 */
class ExtractNetworkUsageData {

    /**
     * Produces a [DashboardData.NetworkUsageData] from information retrieved from the server.
     *
     * @param adapterGraphs A list of [ReportingGraphData] containing information about each network
     * adapter in the system.
     * @param uid A unique identifier for the item that will be returned.
     */
    operator fun invoke(
        adapterGraphs: List<ReportingGraphData>,
        uid: String,
    ): DashboardData.NetworkUsageData {
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
        return DashboardData.NetworkUsageData(uid = uid, adaptersInfo)
    }
}
