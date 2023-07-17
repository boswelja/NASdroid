package com.boswelja.truemanager.apps.ui

import com.boswelja.truemanager.apps.logic.AppsLogicModule
import com.boswelja.truemanager.apps.ui.installed.InstalledAppsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the apps dependency graph.
 */
val AppsModule = module {
    loadKoinModules(AppsLogicModule)

    viewModelOf(::InstalledAppsViewModel)
}
