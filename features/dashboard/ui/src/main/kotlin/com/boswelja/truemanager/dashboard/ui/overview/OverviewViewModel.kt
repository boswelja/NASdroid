package com.boswelja.truemanager.dashboard.ui.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boswelja.truemanager.dashboard.business.DashboardData
import com.boswelja.truemanager.dashboard.business.GetDashboardData
import com.boswelja.truemanager.dashboard.business.InitializeDashboard
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Collects data for and handles events from the Dashboard overview screen.
 */
class OverviewViewModel(
    private val initializeDashboard: InitializeDashboard,
    private val getDashboardData: GetDashboardData
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
}
