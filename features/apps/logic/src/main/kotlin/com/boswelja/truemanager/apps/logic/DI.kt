package com.boswelja.truemanager.apps.logic

import com.boswelja.truemanager.apps.logic.available.GetAllAvailableApps
import com.boswelja.truemanager.apps.logic.available.GetAvailableApps
import com.boswelja.truemanager.apps.logic.available.InstallApplication
import com.boswelja.truemanager.apps.logic.installed.DeleteApp
import com.boswelja.truemanager.apps.logic.installed.GetAppLogs
import com.boswelja.truemanager.apps.logic.installed.GetInstalledApps
import com.boswelja.truemanager.apps.logic.installed.GetLogOptions
import com.boswelja.truemanager.apps.logic.installed.RollbackApp
import com.boswelja.truemanager.apps.logic.installed.StartApp
import com.boswelja.truemanager.apps.logic.installed.StopApp
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the apps logic dependency graph.
 */
val AppsLogicModule = module {
    factoryOf(::GetAllAvailableApps)
    factoryOf(::GetAvailableApps)
    factoryOf(::InstallApplication)

    factoryOf(::DeleteApp)
    factoryOf(::GetAppLogs)
    factoryOf(::GetInstalledApps)
    factoryOf(::GetLogOptions)
    factoryOf(::RollbackApp)
    factoryOf(::StartApp)
    factoryOf(::StopApp)
}
