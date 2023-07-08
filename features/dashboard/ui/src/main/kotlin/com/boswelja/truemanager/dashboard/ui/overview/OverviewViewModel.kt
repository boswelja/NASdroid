package com.boswelja.truemanager.dashboard.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.dashboard.logic.dataloading.DashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.GetDashboardData
import com.boswelja.truemanager.dashboard.logic.configuration.InitializeDashboard
import com.boswelja.truemanager.dashboard.logic.configuration.MoveDashboardEntry
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
    private val moveDashboardEntry: MoveDashboardEntry,
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

    fun moveDashboardEntry(from: Int, to: Int) {
        viewModelScope.launch {
            moveDashboardEntry.invoke(from, to)
        }
    }

    fun showDashboardEntry(position: Int) {
        viewModelScope.launch {
            setDashboardEntryVisible(position, true)
        }
    }

    fun hideDashboardEntry(position: Int) {
        viewModelScope.launch {
            setDashboardEntryVisible(position, false)
        }
    }
}