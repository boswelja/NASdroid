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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    private val _availableDevicesState = MutableStateFlow<FilterOptionState>(FilterOptionState.NoOptions)
    private val _availableMetricsState = MutableStateFlow<FilterOptionState>(FilterOptionState.NoOptions)
    private val _selectedDevices = MutableStateFlow<List<String>>(emptyList())
    private val _selectedMetrics = MutableStateFlow<List<String>>(emptyList())

    /**
     * Flows the currently selected [ReportingCategory].
     */
    val category: StateFlow<ReportingCategory> = _category
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ReportingCategory.CPU
        )

    /**
     * Flows a [FilterOptionState] representing the available devices filtering options.
     */
    val availableDevices: StateFlow<FilterOptionState> = _availableDevicesState

    /**
     * Flows a [FilterOptionState] representing the available metrics filtering options.
     */
    val availableMetrics: StateFlow<FilterOptionState> = _availableMetricsState

    /**
     * Flows a list of currently selected devices to be filtered by.
     */
    val selectedDevices: StateFlow<List<String>> = _selectedDevices

    /**
     * Flows a list of currently selected metrics to be filtered by.
     */
    val selectedMetrics: StateFlow<List<String>> = _selectedMetrics

    @OptIn(ExperimentalCoroutinesApi::class)
    val graphs: StateFlow<List<ReportingGraph>> = _category
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
                    getDiskGraphs(selectedDevices.value).fold(
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
                    getNetworkGraphs(selectedDevices.value).fold(
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

    /**
     * Sets the currently selected category. This triggers an update on [category],
     * [availableDevices], [availableMetrics], [selectedDevices] and [selectedMetrics].
     */
    fun setCategory(category: ReportingCategory) {
        viewModelScope.launch {
            _category.value = category
            updateAvailableDevices(category)
            updateAvailableMetrics()
        }
    }

    private suspend fun updateAvailableDevices(category: ReportingCategory) {
        _availableDevicesState.value = FilterOptionState.Loading
        _selectedDevices.value = emptyList()
        val availableDeviceResult = when (category) {
            ReportingCategory.DISK -> getDisks()
            ReportingCategory.NETWORK -> getNetworkInterfaces()
            ReportingCategory.CPU,
            ReportingCategory.MEMORY,
            ReportingCategory.SYSTEM,
            ReportingCategory.ZFS -> null
        }
        _availableDevicesState.value = availableDeviceResult?.fold(
            onSuccess = { availableDevices ->
                if (availableDevices.size <= 1) {
                    FilterOptionState.NoOptions
                } else {
                    _selectedDevices.value = availableDevices
                    FilterOptionState.HasOptions(availableDevices)
                }
            },
            onFailure = {
                when (it) {
                    ReportingIdentifiersError.NoGraphFound -> FilterOptionState.Error.NoGraphFound
                }
            }
        ) ?: FilterOptionState.NoOptions
    }

    private fun updateAvailableMetrics() {
        _availableMetricsState.value = FilterOptionState.NoOptions
        _selectedMetrics.value = emptyList()
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

/**
 * Encapsulates all possible categories for all reporting graphs.
 */
enum class ReportingCategory {
    CPU,
    DISK,
    MEMORY,
    NETWORK,
    SYSTEM,
    ZFS
}

/**
 * Encapsulates all possible states for an additional filter option category. For example, a
 * [ReportingCategory] might allow filtering by physical device, which would be exposed here.
 */
sealed interface FilterOptionState {

    /**
     * Additional filter options are currently loading.
     */
    data object Loading : FilterOptionState

    /**
     * There are available options for the filter.
     *
     * @property availableOptions A list of all available options.
     */
    data class HasOptions(
        val availableOptions: List<String>
    ) : FilterOptionState

    /**
     * There are no selectable filter options.
     */
    data object NoOptions : FilterOptionState

    /**
     * Encapsulates all possible errors that occurred when trying to retrieve additional filter
     * options.
     */
    sealed interface Error : FilterOptionState {

        /**
         * Indicates that there was no matching graph found when trying to look up options.
         */
        data object NoGraphFound : Error
    }
}
