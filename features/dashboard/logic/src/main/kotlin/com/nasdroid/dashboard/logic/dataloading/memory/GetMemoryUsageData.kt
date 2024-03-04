package com.nasdroid.dashboard.logic.dataloading.memory

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.megabytes
import com.nasdroid.capacity.CapacityUnit
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

/**
 * Retrieves memory-related utilisation data. See [invoke] for details.
 */
class GetMemoryUsageData(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Returns a [Result] that contains either [MemoryUsageData], or an exception if the request
     * failed.
     */
    suspend operator fun invoke(): Result<MemoryUsageData> {
        return try {
            val now = Clock.System.now()
            val graph = reportingV2Api.getGraphData(
                graphs = listOf(RequestedGraph(MEMORY_GRAPH_NAME, null)),
                start = now - 20.seconds,
                end = now,
            ).first()
            val freeIndex = graph.legend.indexOf("free")
            val usedIndex = graph.legend.indexOf("used")
            val cachedIndex = graph.legend.indexOf("cached")
            val buffersIndex = graph.legend.indexOf("buffers")
            val memoryData = graph.data.last { !it.contains(null) } as List<Double>
            Result.success(
                MemoryUsageData(
                    used = memoryData[usedIndex].megabytes,
                    free = memoryData[freeIndex].megabytes,
                    cached = memoryData[cachedIndex].megabytes + memoryData[buffersIndex].megabytes
                )
            )
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }

    companion object {
        private const val MEMORY_GRAPH_NAME = "memory"
    }
}

/**
 * Describes the state of system memory utilisation.
 *
 * @property used The [Capacity] of memory actively in use.
 * @property free The [Capacity] of free memory.
 * @property cached The [Capacity] of memory that is used for cache.
 */
data class MemoryUsageData(
    val used: Capacity,
    val free: Capacity,
    val cached: Capacity,
) {
    /**
     * The total capacity of the system memory, as measured by the sum of all parts.
     */
    val total: Capacity = used + free + cached

    val usedPercent: Float = (used.toLong(CapacityUnit.BYTE) / total.toDouble(CapacityUnit.BYTE)).toFloat()
    val cachedPercent: Float = (cached.toLong(CapacityUnit.BYTE) / total.toDouble(CapacityUnit.BYTE)).toFloat()
}
