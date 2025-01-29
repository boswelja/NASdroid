package com.nasdroid.api.websocket.reporting

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API "Reporting" group.
 */
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

    /**
     * Get a list of all graphs that can be queried from the system.
     */
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
    suspend fun update(tier1Days: Int): ReportingConfig

    /**
     * Retrieve currently running processes stats.
     */
    fun processes(): Flow<RealtimeProcesses>

    /**
     * Retrieve real time statistics for CPU, network, virtual memory and zfs arc.
     */
    fun realtime(interval: Int = 2): Flow<RealtimeUtilisation>
}

@Serializable
data class RealtimeUtilisation(
    @SerialName("cpu")
    val cpu: CpuUsage,
    @SerialName("disks")
    val disks: DiskUsage,
    @SerialName("interfaces")
    val interfaces: InterfaceUsage,
    @SerialName("memory")
    val memory: MemoryUsage,
    @SerialName("virtual_memory")
    val virtualMemory: MemoryUsage,
    @SerialName("zfs")
    val zfs: ZfsUsage,
) {
    @Serializable
    data object CpuUsage // TODO

    @Serializable
    data class DiskUsage(
        @SerialName("busy")
        val busy: Float,
        @SerialName("read_bytes")
        val readBytes: Double,
        @SerialName("write_bytes")
        val writeBytes: Double,
        @SerialName("read_ops")
        val readOps: Double,
        @SerialName("write_ops")
        val writeOps: Double
    )

    @Serializable
    data object InterfaceUsage // TODO

    @Serializable
    data class MemoryUsage(
        @SerialName("apps")
        val apps: Long,
        @SerialName("arc")
        val arc: Long,
        @SerialName("buffers")
        val buffers: Long,
        @SerialName("cache")
        val cache: Long,
        @SerialName("page_tables")
        val pageTables: Long,
        @SerialName("slab_cache")
        val slabCache: Long,
        @SerialName("unused")
        val unused: Long
    )

    @Serializable
    data class ZfsUsage(
        @SerialName("arc_max_size")
        val arcMaxSize: Long,
        @SerialName("arc_size")
        val arcSize: Long,
        @SerialName("cache_hit_ratio")
        val cacheHitRatio: Float
    )
}

@Serializable
data class RealtimeProcesses(
    @SerialName("interval")
    val interval: Int,
    @SerialName("cpu_percent")
    val cpuPercent: Float,
    @SerialName("memory_percent")
    val memoryPercent: Float
)

/**
 * A configuration for NetData reporting.
 *
 * @property id A unique identifier for this configuration.
 * @property tier0Days TODO
 * @property tier1Days The number of days that reporting data is kept for.
 * @property tier1UpdateInterval TODO
 */
@Serializable
data class ReportingConfig(
    @SerialName("id")
    val id: Int,
    @SerialName("tier0_days")
    val tier0Days: Int,
    @SerialName("tier1_days")
    val tier1Days: Int,
    @SerialName("tier1_update_interval")
    val tier1UpdateInterval: Int
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
)
