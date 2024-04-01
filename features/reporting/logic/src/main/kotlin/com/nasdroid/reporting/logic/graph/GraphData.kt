package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingGraphData
import kotlinx.datetime.Instant
import kotlin.math.max

/**
 * Holds all data needed to display a graph.
 *
 * @param T The type of data that the graph contained.
 * @property dataSlices A list of [DataSlice]s.
 * @property legend A list of labels for each line on the graph.
 * @property name The name of the graph. For example, "cpu".
 * @property identifier The identifier used to retrieve the graph data. For example, this could be
 * one specific disk installed in the system.
 * @property start The [Instant] that this graph data starts at.
 * @property end The [Instant] that this graph data ends at.
 */
data class GraphData<T>(
    val dataSlices: List<DataSlice<T>>,
    val legend: List<String>,
    val name: String,
    val identifier: String?,
    val start: Instant,
    val end: Instant
) {
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

    companion object {

        private const val MAX_RESOLUTION = 120

        internal fun <T> ReportingGraphData.toGraphData(
            dropLines: List<String> = emptyList(),
            dataToType: (List<Double>) -> List<T>
        ): GraphData<T> {
            val keepEvery = max(data.size, MAX_RESOLUTION) / MAX_RESOLUTION
            val formattedName = identifier?.let {
                name.replace("{identifier}", it)
            } ?: name
            val dropLineIndices = dropLines.map { legend.indexOf(it) }
            return GraphData(
                dataSlices = data
                    .mapIndexedNotNull { index, data ->
                        if (index % keepEvery == 0) {
                            val dataNoNulls = data.requireNoNulls()
                                .filterIndexed { pointIndex, _ ->
                                    !dropLineIndices.contains(pointIndex)
                                }
                            DataSlice(
                                timestamp = Instant.fromEpochSeconds(dataNoNulls.first().toLong()),
                                data = dataToType(dataNoNulls.drop(1))
                            )
                        } else {
                            null
                        }
                    },
                legend = legend.drop(1).filterNot { dropLines.contains(it) },
                name = formattedName,
                identifier = identifier,
                start = Instant.fromEpochMilliseconds(start),
                end = Instant.fromEpochMilliseconds(end)
            )
        }
    }
}
