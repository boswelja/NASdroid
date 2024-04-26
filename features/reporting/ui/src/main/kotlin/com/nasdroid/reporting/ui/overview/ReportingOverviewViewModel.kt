package com.nasdroid.reporting.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.core.strongresult.fold
import com.nasdroid.reporting.logic.graph.GetDisks
import com.nasdroid.reporting.logic.graph.GetNetworkInterfaces
import com.nasdroid.reporting.logic.graph.ReportingIdentifiersError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A ViewModel that provides the necessary data for the reporting dashboard.
 */
class ReportingOverviewViewModel(
    private val getDisks: GetDisks,
    private val getNetworkInterfaces: GetNetworkInterfaces,
) : ViewModel() {
    private val _availableDevicesState = MutableStateFlow<FilterOptionState>(FilterOptionState.NoOptions)
    private val _availableMetricsState = MutableStateFlow<FilterOptionState>(FilterOptionState.NoOptions)
    private val _selectedDevices = MutableStateFlow<List<String>>(emptyList())
    private val _selectedMetrics = MutableStateFlow<List<String>>(emptyList())
    private val _category = MutableStateFlow(ReportingCategory.CPU)

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
