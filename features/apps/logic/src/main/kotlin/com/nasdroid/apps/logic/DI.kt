package com.nasdroid.apps.logic

import com.nasdroid.apps.data.AppsDataModule
import com.nasdroid.apps.logic.discover.GetAvailableAppDetails
import com.nasdroid.apps.logic.discover.GetAvailableApps
import com.nasdroid.apps.logic.discover.GetAvailableCatalogs
import com.nasdroid.apps.logic.discover.GetAvailableCategories
import com.nasdroid.apps.logic.discover.GetSimilarApps
import com.nasdroid.apps.logic.discover.StripHtmlTags
import com.nasdroid.apps.logic.installed.DeleteApp
import com.nasdroid.apps.logic.installed.GetAppLogs
import com.nasdroid.apps.logic.installed.GetInstalledApp
import com.nasdroid.apps.logic.installed.GetInstalledApps
import com.nasdroid.apps.logic.installed.GetLogOptions
import com.nasdroid.apps.logic.installed.GetRollbackOptions
import com.nasdroid.apps.logic.installed.RollbackApp
import com.nasdroid.apps.logic.installed.StartApp
import com.nasdroid.apps.logic.installed.StopApp
import kotlinx.datetime.Clock
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the apps logic dependency graph.
 */
val AppsLogicModule = module {
    includes(AppsDataModule)

    single { Clock.System } bind Clock::class

    factoryOf(::GetAvailableAppDetails)
    factoryOf(::GetAvailableApps)
    factoryOf(::GetAvailableCatalogs)
    factoryOf(::GetAvailableCategories)
    factoryOf(::GetSimilarApps)
    factoryOf(::StripHtmlTags)

    factoryOf(::DeleteApp)
    factoryOf(::GetAppLogs)
    factoryOf(::GetInstalledApp)
    factoryOf(::GetInstalledApps)
    factoryOf(::GetLogOptions)
    factoryOf(::GetRollbackOptions)
    factoryOf(::RollbackApp)
    factoryOf(::StartApp)
    factoryOf(::StopApp)
}
