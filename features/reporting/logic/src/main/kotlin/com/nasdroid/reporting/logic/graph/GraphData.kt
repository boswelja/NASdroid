package com.nasdroid.reporting.logic.graph

import kotlinx.datetime.Instant

data class GraphData(
    val dataSlices: List<DataSlice>,
    val legend: List<String>,
    val name: String,
    val identifier: String?,
    val start: Instant,
    val end: Instant
) {
    data class DataSlice(
        val timestamp: Instant,
        val data: List<Double>
    )
}
