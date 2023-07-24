package com.boswelja.truemanager.apps.logic

import com.boswelja.truemanager.apps.logic.installed.DeleteApp
import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import com.boswelja.truemanager.apps.logic.installed.RollbackApp
import com.boswelja.truemanager.apps.logic.installed.StartApp
import com.boswelja.truemanager.apps.logic.installed.StopApp
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the apps logic dependency graph.
 */
val AppsLogicModule = module {
    factoryOf(::DeleteApp)
    factoryOf(::GetInstalledApps)
    factoryOf(::RollbackApp)
    factoryOf(::StartApp)
    factoryOf(::StopApp)
}
