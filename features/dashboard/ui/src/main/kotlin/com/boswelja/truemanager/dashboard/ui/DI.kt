package com.boswelja.truemanager.dashboard.ui

import com.boswelja.truemanager.dashboard.ui.overview.OverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the dashboard dependency graph.
 */
val DashboardModule = module {
    viewModelOf(::OverviewViewModel)
}
