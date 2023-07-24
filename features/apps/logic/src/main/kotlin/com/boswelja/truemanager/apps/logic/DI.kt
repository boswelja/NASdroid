package com.boswelja.truemanager.apps.logic

import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import com.boswelja.truemanager.apps.logic.installed.GetLogOptions
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the apps logic dependency graph.
 */
val AppsLogicModule = module {
    factoryOf(::GetInstalledApps)
    factoryOf(::GetLogOptions)
}
