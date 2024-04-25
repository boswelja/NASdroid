package com.nasdroid.reporting.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.capacity.Capacity
import com.boswelja.percentage.Percentage
import com.boswelja.temperature.Temperature
import com.nasdroid.core.strongresult.fold
import com.nasdroid.reporting.logic.graph.GetCpuGraphs
import com.nasdroid.reporting.logic.graph.GetDiskGraphs
import com.nasdroid.reporting.logic.graph.GetDisks
import com.nasdroid.reporting.logic.graph.GetMemoryGraphs
import com.nasdroid.reporting.logic.graph.GetNetworkGraphs
import com.nasdroid.reporting.logic.graph.GetNetworkInterfaces
import com.nasdroid.reporting.logic.graph.GetSystemGraphs
import com.nasdroid.reporting.logic.graph.GetZfsGraphs
import com.nasdroid.reporting.logic.graph.GraphData
import com.nasdroid.reporting.logic.graph.ReportingIdentifiersError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration

/**
 * A ViewModel that provides the necessary data for the reporting dashboard.
 */
class ReportingOverviewViewModel(
    private val getCpuGraphs: GetCpuGraphs,
    private val getDiskGraphs: GetDiskGraphs,
    private val getDisks: GetDisks,
    private val getMemoryGraphs: GetMemoryGraphs,
    private val getNetworkGraphs: GetNetworkGraphs,
    private val getNetworkInterfaces: GetNetworkInterfaces,
    private val getSystemGraphs: GetSystemGraphs,
    private val getZfsGraphs: GetZfsGraphs
) : ViewModel() {
    private val _category = MutableStateFlow(ReportingCategory.CPU)

    val category: StateFlow<ReportingCategory> = _category
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ReportingCategory.CPU
        )

    val extraOptions: StateFlow<Map<String, Boolean>> = _category
        .mapLatest {
            val optionsResult = when (it) {
                ReportingCategory.CPU -> null
                ReportingCategory.DISK -> getDisks()
                ReportingCategory.MEMORY -> null
                ReportingCategory.NETWORK -> getNetworkInterfaces()
                ReportingCategory.SYSTEM -> null
                ReportingCategory.ZFS -> null
            }
            optionsResult?.fold(
                onSuccess = {
                    it.associateWith { true }
                },
                onFailure = {
                    when (it) {
                        ReportingIdentifiersError.NoGroupFound -> TODO()
                    }
                    null
                }
            ).orEmpty()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val graphs: StateFlow<List<ReportingGraph>> = extraOptions
        .map { FilterState(category.value, it) }
        .mapLatest {
            when (it.category) {
                ReportingCategory.CPU ->
                    getCpuGraphs().fold(
                        onSuccess = {
                            listOf(
                                ReportingGraph.PercentageGraph(it.cpuUsageGraph),
                                ReportingGraph.TemperatureGraph(it.cpuTempGraph),
                                ReportingGraph.ProcessesGraph(it.systemLoadGraph)
                            )
                        },
                        onFailure = { emptyList() }
                    )
                ReportingCategory.DISK ->
                    getDiskGraphs(it.extraOptions.filter { it.value }.keys.toList()).fold(
                        onSuccess = {
                            it.diskTemperatures.map {
                                ReportingGraph.TemperatureGraph(it)
                            } + it.diskUtilisations.map {
                                ReportingGraph.CapacityPerSecondGraph(it)
                            }
                        },
                        onFailure = { emptyList() }
                    )
                ReportingCategory.MEMORY ->
                    getMemoryGraphs().fold(
                        onSuccess = {
                            listOf(
                                ReportingGraph.CapacityGraph(it.memoryUtilisation),
                                ReportingGraph.CapacityGraph(it.swapUtilisation)
                            )
                        },
                        onFailure = { emptyList() }
                    )
                ReportingCategory.NETWORK ->
                    getNetworkGraphs(it.extraOptions.filter { it.value }.keys.toList()).fold(
                        onSuccess = {
                            it.networkInterfaces.map {
                                ReportingGraph.BitrateGraph(it)
                            }
                        },
                        onFailure = { emptyList() }
                    )
                ReportingCategory.SYSTEM ->
                    getSystemGraphs().fold(
                        onSuccess = {
                            listOf(
                                ReportingGraph.DurationGraph(it.uptime)
                            )
                        },
                        onFailure = { emptyList() }
                    )
                ReportingCategory.ZFS ->
                    getZfsGraphs().fold(
                        onSuccess = {
                            listOf(
                                ReportingGraph.EventsPerSecondGraph(it.actualCacheHitRate),
                                ReportingGraph.EventsPerSecondGraph(it.arcHitRate),
                                ReportingGraph.CapacityGraph(it.arcSize),
                                ReportingGraph.PercentageGraph(it.arcDemandResult),
                                ReportingGraph.PercentageGraph(it.arcPrefetchResult)
                            )
                        },
                        onFailure = { emptyList() }
                    )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    fun setCategory(category: ReportingCategory) {
        _category.value = category
    }
}

sealed interface ReportingGraph {
    data class TemperatureGraph(
        val data: GraphData<Temperature>
    ): ReportingGraph

    data class PercentageGraph(
        val data: GraphData<Percentage>
    ): ReportingGraph

    data class ProcessesGraph(
        val data: GraphData<Float>
    ): ReportingGraph

    data class CapacityPerSecondGraph(
        val data: GraphData<Capacity>
    ): ReportingGraph

    data class CapacityGraph(
        val data: GraphData<Capacity>
    ): ReportingGraph

    data class BitrateGraph(
        val data: GraphData<Capacity>
    ): ReportingGraph

    data class DurationGraph(
        val data: GraphData<Duration>
    ): ReportingGraph

    data class EventsPerSecondGraph(
        val data: GraphData<Float>
    ): ReportingGraph
}

data class FilterState(
    val category: ReportingCategory,
    val extraOptions: Map<String, Boolean>
)

enum class ReportingCategory {
    CPU,
    DISK,
    MEMORY,
    NETWORK,
    SYSTEM,
    ZFS
}
