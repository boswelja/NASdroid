package com.nasdroid.reporting.logic.graph

import kotlinx.datetime.Instant

/**
 * Holds all data needed to display a graph.
 *
 * @property dataSlices A list of [DataSlice]s.
 * @property legend A list of labels for each line on the graph.
 * @property name The name of the graph. For example, "cpu".
 * @property identifier The identifier used to retrieve the graph data. For example, this could be
 * one specific disk installed in the system.
 * @property start The [Instant] that this graph data starts at.
 * @property end The [Instant] that this graph data ends at.
 */
data class GraphData(
    val dataSlices: List<DataSlice>,
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
     * @property timestamp The [Instant] that [data] occurred at.
     * @property data A list of values that occurred at exactly [timestamp].
     */
    data class DataSlice(
        val timestamp: Instant,
        val data: List<Double>
    )
}
