package com.nasdroid.dashboard.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.dashboard.logic.configuration.DashboardItem
import com.nasdroid.dashboard.logic.configuration.GetDashboardItems
import com.nasdroid.dashboard.logic.configuration.InitializeDashboard
import com.nasdroid.dashboard.logic.configuration.ReorderDashboardItems
import com.nasdroid.dashboard.logic.configuration.SaveDashboardOrder
import com.nasdroid.dashboard.logic.dataloading.DashboardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Collects data for and handles events from the Dashboard overview screen.
 */
class OverviewViewModel(
    private val initializeDashboard: InitializeDashboard,
    private val reorderDashboardItems: ReorderDashboardItems,
    private val saveDashboardOrder: SaveDashboardOrder,
    private val getDashboardItems: GetDashboardItems,
) : ViewModel() {

    private val _editingList = MutableStateFlow<List<DashboardItem>?>(null)

    /**
     * Whether dashboard data is currently being edited.
     */
    val editingList: StateFlow<List<DashboardItem>?> = _editingList
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    /**
     * A List of [DashboardData]. The list is ordered by the users configured display order. If the
     * value is null, data is still loading.
     */
    val dashboardData: StateFlow<Result<List<DashboardItem>>?> = getDashboardItems()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    init {
        viewModelScope.launch {
            initializeDashboard()
        }
    }

    /**
     * Starts "editing mode" for the UI.
     */
    fun startEditing() {
        // Freeze dashboard data for editing
        _editingList.value = dashboardData.value?.getOrNull()
    }

    /**
     * Stops editing mode and saves any pending changes.
     */
    fun stopEditing() {
        // TODO Save the list
        _editingList.value = null
    }

    /**
     * Moves the entry at the specified position to a new position. See [ReorderDashboardItems] for
     * details.
     */
    fun moveDashboardEntry(from: Int, to: Int) {
        _editingList.update { editingList ->
            editingList?.let { reorderDashboardItems(it, from, to) }
        }
        viewModelScope.launch {
            _editingList.value?.let {
                saveDashboardOrder(it)
            }
        }
    }
}
