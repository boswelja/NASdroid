package com.boswelja.truemanager.core.api.v2.reporting

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class ReportingV2ApiImpl(
    private val client: HttpClient
) : ReportingV2Api {
    override suspend fun getReportingDatabaseSettings(): ReportingConfig {
        val response = client.get("/reporting")
        val dto: ReportingConfigDto = response.body()
        return ReportingConfig(
            id = dto.id,
            cpuInPercentage = dto.cpuInPercentage,
            graphiteInstanceUrl = dto.graphiteInstanceUrl,
            graphAge = dto.graphAge,
            graphPoints = dto.graphPoints,
            graphiteSeparateInstances = dto.graphiteSeparateInstances
        )
    }

    override suspend fun setReportingDatabaseSettings(newConfig: ReportingConfig) {
        client.put("/reporting") {
            contentType(ContentType.Application.Json)
            setBody(
                PutReportingConfigDto(
                    cpuInPercentage = newConfig.cpuInPercentage,
                    graphiteInstanceUrl = newConfig.graphiteInstanceUrl,
                    graphAge = newConfig.graphAge,
                    graphPoints = newConfig.graphPoints,
                    graphiteSeparateInstances = newConfig.graphiteSeparateInstances
                )
            )
        }
    }

    override suspend fun clearReportingDatabase() {
        client.get("/reporting/clear")
    }

    override suspend fun getReportingGraphs(
        limit: Int?,
        offset: Int?,
        sort: String?
    ): List<ReportingGraph> {
        val response = client.get("/reporting/graphs") {
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort", sort)
        }
        val dtos: List<ReportingGraphDto> = response.body()
        return dtos.map {
            ReportingGraph(
                name = it.name,
                title = it.title,
                verticalLabel = it.verticalLabel,
                identifiers = it.identifiers.orEmpty(),
                stacked = it.stacked,
                stackedShowTotal = it.stackedShowTotal
            )
        }
    }

    override suspend fun getGraphData(
        graphs: List<String>,
        unit: String,
        page: Int
    ): List<ReportingGraphData> {
        val response = client.post("/reporting/get_data") {
            contentType(ContentType.Application.Json)
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs.map { name ->
                        ReportingGraphDataRequestDto.GraphNameDto(name, null)
                    },
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(null, null, unit, page)
                )
            )
        }
        val dtos: List<ReportingGraphDataDto> = response.body()
        return dtos.map { data ->
            ReportingGraphData(
                name = data.name,
                identifier = data.identifier,
                data = data.data,
                start = data.start,
                end = data.end,
                step = data.step,
                legend = data.legend,
                aggregations = data.aggregations?.let { aggregations ->
                    ReportingGraphData.Aggregations(
                        min = aggregations.min,
                        max = aggregations.max,
                        mean = aggregations.mean
                    )
                }
            )
        }
    }

    override suspend fun getGraphData(
        graphs: List<String>,
        start: Instant,
        end: Instant
    ): List<ReportingGraphData> {
        val response = client.post("/reporting/get_data") {
            contentType(ContentType.Application.Json)
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs.map { name ->
                        ReportingGraphDataRequestDto.GraphNameDto(name, null)
                    },
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(start.epochSeconds, end.epochSeconds, null, null)
                )
            )
        }
        val dtos: List<ReportingGraphDataDto> = response.body()
        return dtos.map { data ->
            ReportingGraphData(
                name = data.name,
                identifier = data.identifier,
                data = data.data,
                start = data.start,
                end = data.end,
                step = data.step,
                legend = data.legend,
                aggregations = data.aggregations?.let { aggregations ->
                    ReportingGraphData.Aggregations(
                        min = aggregations.min,
                        max = aggregations.max,
                        mean = aggregations.mean
                    )
                }
            )
        }
    }

    override suspend fun getGraphData(graphs: List<String>): List<ReportingGraphData> {
        val response = client.post("/reporting/get_data") {
            contentType(ContentType.Application.Json)
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs.map { name ->
                        ReportingGraphDataRequestDto.GraphNameDto(name, null)
                    },
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(null, null, null, null)
                )
            )
        }
        val dtos: List<ReportingGraphDataDto> = response.body()
        return dtos.map { data ->
            ReportingGraphData(
                name = data.name,
                identifier = data.identifier,
                data = data.data,
                start = data.start,
                end = data.end,
                step = data.step,
                legend = data.legend,
                aggregations = data.aggregations?.let { aggregations ->
                    ReportingGraphData.Aggregations(
                        min = aggregations.min,
                        max = aggregations.max,
                        mean = aggregations.mean
                    )
                }
            )
        }
    }
}

@Serializable
internal data class ReportingGraphDto(
    @SerialName("name")
    val name: String,
    @SerialName("title")
    val title: String,
    @SerialName("vertical_label")
    val verticalLabel: String,
    @SerialName("identifiers")
    val identifiers: List<String>?,
    @SerialName("stacked")
    val stacked: Boolean,
    @SerialName("stacked_show_total")
    val stackedShowTotal: Boolean,
)

@Serializable
internal data class ReportingConfigDto(
    @SerialName("id")
    val id: Int,
    @SerialName("cpu_in_percentage")
    val cpuInPercentage: Boolean,
    @SerialName("graphite")
    val graphiteInstanceUrl: String,
    @SerialName("graph_age")
    val graphAge: Int,
    @SerialName("graph_points")
    val graphPoints: Int,
    @SerialName("graphite_separateinstances")
    val graphiteSeparateInstances: Boolean,
)

@Serializable
internal data class PutReportingConfigDto(
    @SerialName("cpu_in_percentage")
    val cpuInPercentage: Boolean,
    @SerialName("graphite")
    val graphiteInstanceUrl: String,
    @SerialName("graph_age")
    val graphAge: Int,
    @SerialName("graph_points")
    val graphPoints: Int,
    @SerialName("graphite_separateinstances")
    val graphiteSeparateInstances: Boolean,
)

@Serializable
internal data class ReportingGraphDataRequestDto(
    @SerialName("graphs")
    val graphs: List<GraphNameDto>,
    @SerialName("reporting_query")
    val reportingQuery: ReportingQuery,
) {
    @Serializable
    internal data class GraphNameDto(
        @SerialName("name")
        val name: String,
        @SerialName("identifier")
        val identifier: String?,
    )

    @Serializable
    internal data class ReportingQuery(
        @SerialName("start")
        val start: Long?,
        @SerialName("end")
        val end: Long?,
        @SerialName("unit")
        val unit: String?,
        @SerialName("page")
        val page: Int?,
    )
}

@Serializable
internal data class ReportingGraphDataDto(
    @SerialName("name")
    val name: String,
    @SerialName("identifier")
    val identifier: String?,
    @SerialName("data")
    val data: List<List<Double?>>,
    @SerialName("start")
    val start: Instant,
    @SerialName("end")
    val end: Instant,
    @SerialName("step")
    val step: Int,
    @SerialName("legend")
    val legend: List<String>,
    @SerialName("aggregations")
    val aggregations: AggregationsDto?
) {
    @Serializable
    internal data class AggregationsDto(
        @SerialName("min")
        val min: List<Int>,
        @SerialName("max")
        val max: List<Int>,
        @SerialName("mean")
        val mean: List<Int>,
    )
}
