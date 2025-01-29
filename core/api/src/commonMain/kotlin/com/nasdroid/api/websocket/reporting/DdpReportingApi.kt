package com.nasdroid.api.websocket.reporting

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.SubscriptionEvent
import com.nasdroid.api.websocket.ddp.callMethod
import com.nasdroid.api.websocket.ddp.subscribe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DdpReportingApi(private val client: DdpWebsocketClient) : ReportingApi {
    override suspend fun config(): ReportingConfig {
        return client.callMethod("reporting.config")
    }

    override suspend fun getData(
        graphs: List<RequestedGraph>,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.get_data",
            params = listOf(
                GraphDataArgs.Graphs(graphs),
                GraphDataArgs.QueryParams(unit = null, page = null, start = null, end = null, aggregate = aggregate)
            )
        )
    }

    override suspend fun getData(
        graphs: List<RequestedGraph>,
        unit: GraphUnit,
        page: Int,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.get_data",
            params = listOf(
                GraphDataArgs.Graphs(graphs),
                GraphDataArgs.QueryParams(unit = unit, page = page, start = null, end = null, aggregate = aggregate)
            )
        )
    }

    override suspend fun getData(
        graphs: List<RequestedGraph>,
        start: Instant,
        end: Instant,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.get_data",
            params = listOf(
                GraphDataArgs.Graphs(graphs),
                GraphDataArgs.QueryParams(
                    unit = null,
                    page = null,
                    start = start.toEpochMilliseconds(),
                    end = end.toEpochMilliseconds(),
                    aggregate = aggregate
                )
            )
        )
    }

    override suspend fun graph(
        name: String,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.graph",
            params = listOf(
                GraphArgs.Name(name),
                GraphArgs.QueryParams(unit = null, page = null, start = null, end = null, aggregate = aggregate)
            )
        )
    }

    override suspend fun graph(
        name: String,
        unit: GraphUnit,
        page: Int,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.graph",
            params = listOf(
                GraphArgs.Name(name),
                GraphArgs.QueryParams(unit = unit, page = page, start = null, end = null, aggregate = aggregate)
            )
        )
    }

    override suspend fun graph(
        name: String,
        start: Instant,
        end: Instant,
        aggregate: Boolean
    ): GraphData {
        return client.callMethod(
            method = "reporting.graph",
            params = listOf(
                GraphArgs.Name(name),
                GraphArgs.QueryParams(
                    unit = null,
                    page = null,
                    start = start.toEpochMilliseconds(),
                    end = end.toEpochMilliseconds(),
                    aggregate = aggregate
                )
            )
        )
    }

    override suspend fun graphs(
        limit: Int,
        offset: Int
    ): List<Graph> {
        return client.callMethod(
            method = "reporting.graphs",
            params = listOf(GraphsArgs.QueryFilters(emptyList()), GraphsArgs.PagingData(limit, offset))
        )
    }

    override suspend fun netDataWebGeneratePassword(): String {
        return client.callMethod("reporting.netdataweb_generate_password")
    }

    override suspend fun update(tier1Days: Int): ReportingConfig {
       return client.callMethod("reporting.update", listOf(mapOf("tier1_days" to tier1Days)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun processes(): Flow<RealtimeProcesses> {
        return client.subscribe<RealtimeProcesses, String>("reporting.realtime", emptyList())
            .filterIsInstance<SubscriptionEvent.DocumentAdded<RealtimeProcesses>>()
            .mapLatest { it.document }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun realtime(interval: Int): Flow<RealtimeUtilisation> {
        return client.subscribe<RealtimeUtilisation, String>("reporting.realtime", emptyList())
            .filterIsInstance<SubscriptionEvent.DocumentAdded<RealtimeUtilisation>>()
            .mapLatest { it.document }
    }
}

@Serializable
internal sealed interface GraphArgs {
    @JvmInline
    @Serializable
    value class Name(val value: String): GraphArgs

    @Serializable
    data class QueryParams(
        @SerialName("unit")
        val unit: GraphUnit?,
        @SerialName("page")
        val page: Int?,
        @SerialName("start")
        val start: Long?,
        @SerialName("end")
        val end: Long?,
        @SerialName("aggregate")
        val aggregate: Boolean
    ) : GraphArgs
}

@Serializable
internal sealed interface GraphsArgs {
    @JvmInline
    @Serializable
    value class QueryFilters(val value: List<String>): GraphsArgs

    @Serializable
    data class PagingData(
        @SerialName("limit")
        val limit: Int,
        @SerialName("offset")
        val offset: Int
    ): GraphsArgs
}

@Serializable
internal sealed interface GraphDataArgs {
    @JvmInline
    @Serializable
    value class Graphs(val value: List<RequestedGraph>) : GraphDataArgs

    @Serializable
    data class QueryParams(
        @SerialName("unit")
        val unit: GraphUnit?,
        @SerialName("page")
        val page: Int?,
        @SerialName("start")
        val start: Long?,
        @SerialName("end")
        val end: Long?,
        @SerialName("aggregate")
        val aggregate: Boolean
    ) : GraphDataArgs
}