package com.nasdroid.apps.logic

import com.nasdroid.apps.logic.available.GetAllAvailableApps
import com.nasdroid.apps.logic.available.GetAvailableApps
import com.nasdroid.apps.logic.available.InstallApplication
import com.nasdroid.apps.logic.installed.DeleteApp
import com.nasdroid.apps.logic.installed.GetAppLogs
import com.nasdroid.apps.logic.installed.GetInstalledApps
import com.nasdroid.apps.logic.installed.GetLogOptions
import com.nasdroid.apps.logic.installed.RollbackApp
import com.nasdroid.apps.logic.installed.StartApp
import com.nasdroid.apps.logic.installed.StopApp
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
