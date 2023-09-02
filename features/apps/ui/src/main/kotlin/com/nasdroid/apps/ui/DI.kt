package com.nasdroid.apps.ui

import com.nasdroid.apps.logic.AppsLogicModule
import com.nasdroid.apps.ui.available.AvailableAppsViewModel
import com.nasdroid.apps.ui.installed.InstalledAppsViewModel
import com.nasdroid.apps.ui.installed.logs.LogsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the apps dependency graph.
 */
val AppsModule = module {
    loadKoinModules(AppsLogicModule)

    viewModelOf(::AvailableAppsViewModel)

    viewModelOf(::InstalledAppsViewModel)
    viewModelOf(::LogsViewModel)
}
