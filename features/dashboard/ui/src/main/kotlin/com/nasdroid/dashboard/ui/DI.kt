package com.nasdroid.dashboard.ui

import com.nasdroid.dashboard.logic.dashboardBusinessModule
import com.nasdroid.dashboard.ui.overview.OverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the dashboard dependency graph.
 */
val DashboardModule = module {
    loadKoinModules(dashboardBusinessModule)
    viewModelOf(::OverviewViewModel)
}
