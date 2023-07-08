package com.boswelja.truemanager.dashboard.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.dashboard.logic.dataloading.DashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.GetDashboardData
import com.boswelja.truemanager.dashboard.logic.configuration.InitializeDashboard
import com.boswelja.truemanager.dashboard.logic.configuration.ReorderDashboardData
import com.boswelja.truemanager.dashboard.logic.configuration.SetDashboardEntryVisible
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Collects data for and handles events from the Dashboard overview screen.
 */
class OverviewViewModel(
    private val initializeDashboard: InitializeDashboard,
    private val getDashboardData: GetDashboardData,
    private val reorderDashboardData: ReorderDashboardData,
    private val setDashboardEntryVisible: SetDashboardEntryVisible,
) : ViewModel() {

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

    /**
     * Moves the entry at the specified position to a new position. See [ReorderDashboardData] for
     * details.
     */
    fun moveDashboardEntry(from: Int, to: Int) {
        viewModelScope.launch {
            // TODO
        }
    }

    /**
     * Marks the dashboard entry at the specified position as visible, so that it is shown when not
     * editing.
     */
    fun showDashboardEntry(position: Int) {
        viewModelScope.launch {
            setDashboardEntryVisible(position, true)
        }
    }

    /**
     * Marks the dashboard entry at the specified position as gone, so that it is gone shown when
     * not editing.
     */
    fun hideDashboardEntry(position: Int) {
        viewModelScope.launch {
            setDashboardEntryVisible(position, false)
        }
    }
}
