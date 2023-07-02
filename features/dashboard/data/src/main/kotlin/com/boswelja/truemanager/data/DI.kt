package com.boswelja.truemanager.data

import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.database.DashboardConfigurationDatabaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module that defines bindings for data interfaces. This should be loaded when a dependency
 * on data exists.
 */
val dashboardDataModule = module {
    singleOf(::DashboardConfigurationDatabaseImpl) bind DashboardConfiguration::class
}
