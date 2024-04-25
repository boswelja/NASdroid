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

    @OptIn(ExperimentalCoroutinesApi::class)
    val availableOptionsState: StateFlow<FilterOptionsState> = _category
        .mapLatest {
            val availableDeviceResult = when (it) {
                ReportingCategory.CPU -> null
                ReportingCategory.DISK -> getDisks()
                ReportingCategory.MEMORY -> null
                ReportingCategory.NETWORK -> getNetworkInterfaces()
                ReportingCategory.SYSTEM -> null
                ReportingCategory.ZFS -> null
            }
            availableDeviceResult?.fold(
                onSuccess = {
                    if (it.size <= 1) {
                        FilterOptionsState.NoOptions
                    } else {
                        FilterOptionsState.HasOptions(it, emptyList())
                    }
                },
                onFailure = {
                    when (it) {
                        ReportingIdentifiersError.NoGraphFound -> FilterOptionsState.Error.NoGraphFound
                    }
                }
            ) ?: FilterOptionsState.NoOptions
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FilterOptionsState.NoOptions
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val graphs: StateFlow<List<ReportingGraph>> = availableOptionsState
        .mapLatest {
            when (category.value) {
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
                    getDiskGraphs((it as FilterOptionsState.HasOptions).availableDevices).fold(
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
                    getNetworkGraphs((it as FilterOptionsState.HasOptions).availableDevices).fold(
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

enum class ReportingCategory {
    CPU,
    DISK,
    MEMORY,
    NETWORK,
    SYSTEM,
    ZFS
}

sealed interface FilterOptionsState {
    data class HasOptions(
        val availableDevices: List<Device>,
        val availableMetrics: List<Metric>
    ) : FilterOptionsState {
        data class Device(
            val name: String,
            val selected: Boolean
        )

        data class Metric(
            val name: String,
            val selected: Boolean
        )
    }

    data object NoOptions : FilterOptionsState

    sealed interface Error : FilterOptionsState {

        data object NoGraphFound : Error
    }
}
