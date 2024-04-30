package com.nasdroid.api.v2.reporting

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class ReportingV2ApiImpl(
    private val client: HttpClient
) : ReportingV2Api {
    override suspend fun getReportingDatabaseSettings(): ReportingConfig {
        val response = client.get("reporting")
        return response.body()
    }

    override suspend fun setReportingDatabaseSettings(newConfig: ReportingConfig) {
        client.put("reporting") {
            setBody(newConfig)
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
        val response = client.get("reporting/netdata_graphs") {
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort", sort)
        }
        return response.body()
    }

    override suspend fun getGraphData(
        graphs: List<RequestedGraph>,
        unit: Units,
        page: Int
    ): List<ReportingGraphData> {
        val response = client.post("reporting/netdata_get_data") {
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs,
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(
                        null,
                        null,
                        unit,
                        page
                    )
                )
            )
        }
        return response.body()
    }

    override suspend fun getGraphData(
        graphs: List<RequestedGraph>,
        start: Instant,
        end: Instant
    ): List<ReportingGraphData> {
        val response = client.post("reporting/netdata_get_data") {
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs,
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(
                        start.epochSeconds,
                        end.epochSeconds,
                        null,
                        null
                    )
                )
            )
        }
        return response.body()
    }

    override suspend fun getGraphData(graphs: List<RequestedGraph>): List<ReportingGraphData> {
        val response = client.post("reporting/netdata_get_data") {
            setBody(
                ReportingGraphDataRequestDto(
                    graphs = graphs,
                    reportingQuery = ReportingGraphDataRequestDto.ReportingQuery(null, null, null, null)
                )
            )
        }
        return response.body()
    }
}

@Serializable
internal data class ReportingGraphDataRequestDto(
    @SerialName("graphs")
    val graphs: List<RequestedGraph>,
    @SerialName("reporting_query")
    val reportingQuery: ReportingQuery,
) {
    @Serializable
    internal data class ReportingQuery(
        @SerialName("start")
        val start: Long?,
        @SerialName("end")
        val end: Long?,
        @SerialName("unit")
        val unit: Units?,
        @SerialName("page")
        val page: Int?,
    )
}
