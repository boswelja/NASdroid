package com.nasdroid.dashboard.ui

import com.nasdroid.dashboard.logic.dashboardBusinessModule
import com.nasdroid.dashboard.ui.overview.OverviewViewModel
import com.nasdroid.dashboard.ui.overview.cpu.CpuOverviewViewModel
import com.nasdroid.dashboard.ui.overview.memory.MemoryOverviewViewModel
import com.nasdroid.dashboard.ui.overview.network.NetworkOverviewViewModel
import com.nasdroid.dashboard.ui.overview.system.SystemInformationOverviewViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the dashboard dependency graph.
 */
val DashboardModule = module {
    includes(dashboardBusinessModule)

    viewModelOf(::CpuOverviewViewModel)
    viewModelOf(::MemoryOverviewViewModel)
    viewModelOf(::NetworkOverviewViewModel)
    viewModelOf(::SystemInformationOverviewViewModel)

    viewModelOf(::OverviewViewModel)
}
