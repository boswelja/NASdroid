package com.nasdroid.dashboard.ui

import com.nasdroid.dashboard.logic.dashboardBusinessModule
import com.nasdroid.dashboard.ui.overview.OverviewViewModel
import com.nasdroid.dashboard.ui.overview.cpu.CpuOverviewViewModel
import com.nasdroid.dashboard.ui.overview.memory.MemoryOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the dashboard dependency graph.
 */
val DashboardModule = module {
    loadKoinModules(dashboardBusinessModule)

    viewModelOf(::CpuOverviewViewModel)
    viewModelOf(::MemoryOverviewViewModel)
    viewModelOf(::OverviewViewModel)
}
