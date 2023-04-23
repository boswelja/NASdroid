package com.boswelja.truemanager.core.api.v2.reporting

import kotlinx.datetime.Instant

/**
 * Describes the TrueNAS API V2 "Reporting" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface ReportingV2Api {

    suspend fun getReportingDatabaseSettings(): ReportingConfig

    suspend fun setReportingDatabaseSettings(newConfig: ReportingConfig)

    suspend fun clearReportingDatabase()

    suspend fun getReportingGraphs(
        limit: Int?,
        offset: Int?,
        sort: String?,
    ): List<ReportingGraph>

    suspend fun getGraphData(graphs: List<RequestedGraph>, unit: Units, page: Int): List<ReportingGraphData>

    suspend fun getGraphData(graphs: List<RequestedGraph>, start: Instant, end: Instant): List<ReportingGraphData>

    suspend fun getGraphData(graphs: List<RequestedGraph>): List<ReportingGraphData>
}

data class ReportingConfig(
    val id: Int,
    val cpuInPercentage: Boolean,
    val graphiteInstanceUrl: String,
    val graphMaxAgeMonths: Int,
    val graphPoints: Int,
    val graphiteSeparateInstances: Boolean,
)

data class ReportingGraph(
    val name: String,
    val title: String,
    val verticalLabel: String,
    val identifiers: List<String>,
    val stacked: Boolean,
    val stackedShowTotal: Boolean,
)

data class ReportingGraphData(
    val name: String,
    val identifier: String?,
    val data: List<List<Double?>>,
    val start: Instant,
    val end: Instant,
    val step: Int,
    val legend: List<String>,
    val aggregations: Aggregations?
) {
    data class Aggregations(
        val min: List<Double?>,
        val max: List<Double?>,
        val mean: List<Double?>,
    )
}

data class RequestedGraph(
    val name: String,
    val identifier: String?,
)

enum class Units {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR
}
