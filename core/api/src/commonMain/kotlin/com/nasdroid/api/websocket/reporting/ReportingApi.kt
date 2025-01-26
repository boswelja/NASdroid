package com.nasdroid.api.websocket.reporting

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface ReportingApi {

    /**
     * -
     */
    suspend fun config(): ReportingConfig

    /**
     * Get reporting data for given graphs.
     * List of possible graphs can be retrieved using [graphs].
     * [aggregate] will return aggregate available data for each graph (e.g. min, max, mean).
     */
    suspend fun getData(graphs: List<RequestedGraph>, aggregate: Boolean = true): GraphData

    /**
     * Get reporting data for given graphs.
     * List of possible graphs can be retrieved using [graphs].
     * [aggregate] will return aggregate available data for each graph (e.g. min, max, mean).
     */
    suspend fun getData(
        graphs: List<RequestedGraph>,
        unit: GraphUnit,
        page: Int = 1,
        aggregate: Boolean = true
    ): GraphData

    /**
     * Get reporting data for given graphs.
     * List of possible graphs can be retrieved using [graphs].
     * [aggregate] will return aggregate available data for each graph (e.g. min, max, mean).
     */
    suspend fun getData(
        graphs: List<RequestedGraph>,
        start: Instant,
        end: Instant,
        aggregate: Boolean = true
    ): GraphData

    /**
     * Get reporting data for name graph.
     */
    suspend fun graph(name: String, aggregate: Boolean = true): GraphData

    /**
     * Get reporting data for name graph.
     */
    suspend fun graph(
        name: String,
        unit: GraphUnit,
        page: Int = 1,
        aggregate: Boolean = true
    ): GraphData

    /**
     * Get reporting data for name graph.
     */
    suspend fun graph(
        name: String,
        start: Instant,
        end: Instant,
        aggregate: Boolean = true
    ): GraphData

    suspend fun graphs(
        limit: Int = 0,
        offset: Int = 0,
    ): List<Graph>

    /**
     * Generate a password to access netdata web. That password will be stored in htpasswd format
     * for HTTP Basic access.
     * Concurrent access for the same user is not supported and may lead to undesired behavior.
     */
    suspend fun netDataWebGeneratePassword(): String

    /**
     * [ReportingConfig.tier1Days] can be set to specify for how many days we want to store
     * reporting history which in netdata terms specifies the number of days netdata should be
     * storing data in tier1 storage.
     */
    suspend fun update(config: ReportingConfig): ReportingConfig
}

/**
 * A configuration for NetData reporting.
 *
 * @property tier1Days The number of days that reporting data is kept for.
 */
@Serializable
data class ReportingConfig(
    @SerialName("tier1_days")
    val tier1Days: Int
)

@Serializable
data class RequestedGraph(
    @SerialName("name")
    val name: GraphName,
    @SerialName("identifier")
    val identifier: String?
) {
    @Serializable
    enum class GraphName {
        @SerialName("cpu")
        CpuUsage,
        @SerialName("cputemp")
        CpuTemp,
        @SerialName("disk")
        DiskIo,
        @SerialName("interface")
        InterfaceTraffic,
        @SerialName("load")
        Load,
        @SerialName("processes")
        ActiveProcesses,
        @SerialName("memory")
        MemoryUsage,
        @SerialName("uptime")
        Uptime,
        @SerialName("arcactualrate")
        ArcActualRate,
        @SerialName("arcrate")
        ArcRate,
        @SerialName("arcsize")
        ArcSize,
        @SerialName("arcresult")
        ArcResult,
        @SerialName("disktemp")
        DiskTemp,
        @SerialName("upscharge")
        UpsCharge,
        @SerialName("upsruntime")
        UpsRuntime,
        @SerialName("upsvoltage")
        UpsVoltage,
        @SerialName("upscurrent")
        UpsCurrent,
        @SerialName("upsfrequency")
        UpsFrequency,
        @SerialName("upsload")
        UpsLoad,
        @SerialName("upstemperature")
        UpsTemperature
    }
}

@Serializable
enum class GraphUnit {
    @SerialName("HOUR")
    Hour,
    @SerialName("DAY")
    Day,
    @SerialName("WEEK")
    Week,
    @SerialName("MONTH")
    Month,
    @SerialName("YEAR")
    Year
}

/**
 * Contains data for a single graph.
 *
 * @property name The name of the graph.
 * @property identifier The graph identifier, if any.
 * @property data A 2D array of graph data, where the first dimension contains a number of lines on
 * a graph, and the second contains data for a line.
 * @property start The earliest point in time this has data for.
 * @property end The latest point in time this has data for.
 * @property step TODO
 * @property legend A list of labels for each line. If there is only one line, this may be empty.
 * @property aggregations [Aggregations].
 */
@Serializable
data class GraphData(
    @SerialName("name")
    val name: String,
    @SerialName("identifier")
    val identifier: String?,
    @SerialName("data")
    val data: List<List<Double?>>,
    @SerialName("start")
    val start: Long,
    @SerialName("end")
    val end: Long,
    @SerialName("step")
    val step: Int?,
    @SerialName("legend")
    val legend: List<String>,
    @SerialName("aggregations")
    val aggregations: Aggregations?
) {
    /**
     * Contains lists of various statistics for the data we have.
     *
     * @property min A list of minimum values, one for each line on the graph, or null if there is
     * no data.
     * @property max A list of maximum values, one for each line on the graph, or null if there is
     * no data.
     * @property mean A list of mean average values, one for each line on the graph, or null if
     * there is no data.
     */
    @Serializable
    data class Aggregations(
        @SerialName("min")
        val min: Map<String, Double?>,
        @SerialName("max")
        val max: Map<String, Double?>,
        @SerialName("mean")
        val mean: Map<String, Double?>,
    )
}

/**
 * Describes a reporting graph.
 *
 * @property name The identifier of this reporting graph.
 * @property title A human-readable title for the graph. If there are any identifiers, this will
 * contain `{identifier}`, and should be replaced accordingly.
 * @property verticalLabel The label of the vertical axis.
 * @property identifiers A list of identifiers. If this is not empty, there will be multiple sets of
 * data for this graph. These should be displayed as separate graphs, and each identifier should be
 * inserted into the title.
 * @property items TODO
 */
@Serializable
data class Graph(
    @SerialName("name")
    val name: String,
    @SerialName("title")
    val title: String,
    @SerialName("vertical_label")
    val verticalLabel: String,
    @SerialName("identifiers")
    val identifiers: List<String>?,
    @SerialName("items")
    val items: List<String>?
)
