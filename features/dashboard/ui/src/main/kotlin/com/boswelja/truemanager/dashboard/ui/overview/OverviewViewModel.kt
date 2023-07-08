package com.boswelja.truemanager.dashboard.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.dashboard.logic.configuration.InitializeDashboard
import com.boswelja.truemanager.dashboard.logic.configuration.ReorderDashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.DashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.GetDashboardData
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
    private val getDashboardData: GetDashboardData,
    private val reorderDashboardData: ReorderDashboardData,
) : ViewModel() {

    private val _editingList = MutableStateFlow<List<DashboardData>?>(null)

    /**
     * Whether dashboard data is currently being edited.
     */
    val editingList: StateFlow<List<DashboardData>?> = _editingList
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    /**
     * A List of [DashboardData]. The list is ordered by the users configured display order. If the
     * value is null, data is still loading.
     */
    val dashboardData: StateFlow<List<DashboardData>?> = getDashboardData()
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

    fun startEditing() {
        // Freeze dashboard data for editing
        _editingList.value = dashboardData.value
    }

    fun stopEditing() {
        // TODO save new config
    }

    /**
     * Moves the entry at the specified position to a new position. See [ReorderDashboardData] for
     * details.
     */
    fun moveDashboardEntry(from: Int, to: Int) {
        _editingList.update { editingList ->
            editingList?.let { reorderDashboardData(it, from, to) }
        }
    }
}
