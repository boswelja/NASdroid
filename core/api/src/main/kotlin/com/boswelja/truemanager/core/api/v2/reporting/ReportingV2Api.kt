package com.boswelja.truemanager.core.api.v2.reporting

import kotlinx.datetime.Instant

/**
 * Describes the TrueNAS API V2 "Reporting" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface ReportingV2Api {

    /**
     * Get the configured reporting database settings.
     */
    suspend fun getReportingDatabaseSettings(): ReportingConfig

    /**
     * Update the reporting database with new settings.
     */
    suspend fun setReportingDatabaseSettings(newConfig: ReportingConfig)

    /**
     * Clear the reporting database.
     */
    suspend fun clearReportingDatabase()

    /**
     * Get a list of all available reporting graphs. This does not include data.
     *
     * @param limit The maximum number of graphs to retrieve.
     * @param offset The number of graphs to skip before taking [limit] keys.
     * @param sort The sorting method for graphs.
     */
    suspend fun getReportingGraphs(
        limit: Int?,
        offset: Int?,
        sort: String?,
    ): List<ReportingGraph>

    /**
     * Get data for a list of graphs, paged by their unit.
     *
     * @param graphs The list of [RequestedGraph]s to retrieve data for.
     * @param unit The unit we are interested in.
     * @param page The page number of the data we want.
     *
     * @return A list the same size as [graphs], containing [ReportingGraphData].
     */
    suspend fun getGraphData(graphs: List<RequestedGraph>, unit: Units, page: Int): List<ReportingGraphData>

    /**
     * Get data for a list of graphs, constrained to a time range.
     *
     * @param graphs The list of [RequestedGraph]s to retrieve data for.
     * @param start The approximate starting point of the data we are interested in.
     * @param end The approximate ending point of the data we are interested in.
     *
     * @return A list the same size as [graphs], containing [ReportingGraphData].
     */
    suspend fun getGraphData(graphs: List<RequestedGraph>, start: Instant, end: Instant): List<ReportingGraphData>

    /**
     * Get data for a list of graphs.
     *
     * @param graphs The list of [RequestedGraph]s to retrieve data for.
     *
     * @return A list the same size as [graphs], containing [ReportingGraphData].
     */
    suspend fun getGraphData(graphs: List<RequestedGraph>): List<ReportingGraphData>
}

/**
 * Describes the reporting database configuration.
 *
 * @property id The reporting config ID.
 * @property cpuInPercentage Whether CPU utilisation values should be displayed as a percentage.
 * @property graphiteInstanceUrl The custom Graphite URL, if any.
 * @property graphMaxAgeMonths The maximum number of months the database should keep data for.
 * @property graphPoints The number of points in a graph period.
 * @property graphiteSeparateInstances Whether to use a separate Graphite instance for reporting.
 */
data class ReportingConfig(
    val id: Int,
    val cpuInPercentage: Boolean,
    val graphiteInstanceUrl: String?,
    val graphMaxAgeMonths: Int,
    val graphPoints: Int,
    val graphiteSeparateInstances: Boolean,
)

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
 * @property stacked TODO
 * @property stackedShowTotal TODO
 */
data class ReportingGraph(
    val name: String,
    val title: String,
    val verticalLabel: String,
    val identifiers: List<String>,
    val stacked: Boolean,
    val stackedShowTotal: Boolean,
)

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
    data class Aggregations(
        val min: List<Double?>,
        val max: List<Double?>,
        val mean: List<Double?>,
    )
}

/**
 * Describes a graph which we are requesting data for.
 *
 * @property name The name of the graph.
 * @property identifier The graph identifier. Note one graph can have many identifiers, and should
 * be displayed as separate graphs.
 */
data class RequestedGraph(
    val name: String,
    val identifier: String?,
)

/**
 * Possible units for a graph to be displayed in.
 */
enum class Units {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR
}
