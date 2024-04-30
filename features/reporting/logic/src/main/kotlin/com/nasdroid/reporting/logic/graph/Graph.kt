package com.nasdroid.reporting.logic.graph

import com.boswelja.bitrate.Bitrate
import com.boswelja.bitrate.Bitrate.Companion.kilobits
import com.boswelja.capacity.Capacity
import com.boswelja.capacity.Capacity.Companion.mebibytes
import com.boswelja.percentage.Percentage
import com.boswelja.percentage.Percentage.Companion.percent
import com.boswelja.temperature.Temperature
import com.boswelja.temperature.Temperature.Companion.celsius
import com.nasdroid.api.v2.reporting.ReportingGraphData
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val TEMPORAL_AGGREGATION_SECONDS = 30

/**
 * Holds all data needed to display a graph.
 *
 * @param T The type of data that the graph contained.
 */
sealed interface Graph<T> {
    /**
     * A list of [DataSlice]s.
     */
    val dataSlices: List<DataSlice<T>>

    /**
     * A list of labels for each line on the graph.
     */
    val legend: List<String>

    /**
     * The name of the graph. For example, "cputemp".
     */
    val name: String

    /**
     * The identifier used to retrieve the graph data. For example, this could be one specific disk
     * installed in the system.
     */
    val identifier: String?

    /**
     * The [Instant] that this graph data starts at.
     */
    val start: Instant

    /**
     * The [Instant] that this graph data ends at.
     */
    val end: Instant

    /**
     * A slice of data in a graph. This represents a series of points from multiple lines that have
     * a value at one specific timestamp.
     *
     * @param T The type of data that this slice holds.
     * @property timestamp The [Instant] that [data] occurred at.
     * @property data A list of values that occurred at exactly [timestamp].
     */
    data class DataSlice<T>(
        val timestamp: Instant,
        val data: List<T>
    )
}

/**
 * A [Graph] of [Percentage] values.
 */
data class PercentageGraph(
    override val dataSlices: List<Graph.DataSlice<Percentage>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant
): Graph<Percentage> {
    companion object {
        fun ReportingGraphData.toPercentageGraph(): PercentageGraph {
            return PercentageGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { it.percent }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end)
            )
        }
    }
}

/**
 * A [Graph] of [Temperature] values.
 */
data class TemperatureGraph(
    override val dataSlices: List<Graph.DataSlice<Temperature>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant
): Graph<Temperature> {
    companion object {
        fun ReportingGraphData.toTemperatureGraph(): TemperatureGraph {
            return TemperatureGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { it.celsius }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end)
            )
        }
    }
}

/**
 * A [Graph] of [Capacity] values.
 */
data class CapacityGraph(
    override val dataSlices: List<Graph.DataSlice<Capacity>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant
): Graph<Capacity> {
    companion object {
        fun ReportingGraphData.toCapacityGraph(transform: (Double) -> Capacity = { it.mebibytes }): CapacityGraph {
            return CapacityGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { transform(it) }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end)
            )
        }
    }
}

/**
 * A [Graph] of [Bitrate] values.
 */
data class BitrateGraph(
    override val dataSlices: List<Graph.DataSlice<Bitrate>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant
): Graph<Bitrate> {
    companion object {
        fun ReportingGraphData.toBitrateGraph(): BitrateGraph {
            return BitrateGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { it.kilobits }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end)
            )
        }
    }
}

/**
 * A [Graph] of [Duration] values.
 */
data class DurationGraph(
    override val dataSlices: List<Graph.DataSlice<Duration>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant
): Graph<Duration> {
    companion object {
        fun ReportingGraphData.toDurationGraph(transform: (Double) -> Duration = { it.seconds }): DurationGraph {
            return DurationGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { transform(it) }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end)
            )
        }
    }
}

/**
 * A [Graph] of [Float] values.
 */
data class FloatGraph(
    override val dataSlices: List<Graph.DataSlice<Float>>,
    override val legend: List<String>,
    override val name: String,
    override val identifier: String?,
    override val start: Instant,
    override val end: Instant,
    /**
     * The human-readable label for the graphs vertical axis.
     */
    val verticalLabel: String,
): Graph<Float> {
    companion object {
        fun ReportingGraphData.toFloatGraph(verticalLabel: String): FloatGraph {
            return FloatGraph(
                dataSlices = data
                    .map { data ->
                        val dataNoNulls = data.requireNoNulls()
                        val timestampSeconds = dataNoNulls.first().toLong()
                        val roundedTimestamp = timestampSeconds - (timestampSeconds % TEMPORAL_AGGREGATION_SECONDS)
                        Graph.DataSlice(
                            timestamp = Instant.fromEpochSeconds(roundedTimestamp),
                            data = dataNoNulls.drop(1).map { it.toFloat() }
                        )
                    }
                    .distinctBy { it.timestamp },
                legend = legend.drop(1),
                name = name,
                identifier = identifier,
                start = Instant.fromEpochSeconds(start),
                end = Instant.fromEpochSeconds(end),
                verticalLabel = verticalLabel
            )
        }
    }
}
