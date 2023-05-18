package com.boswelja.truemanager.dashboard

import com.boswelja.truemanager.dashboard.configuration.DashboardConfiguration
import com.boswelja.truemanager.dashboard.configuration.database.DashboardConfigurationDatabaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the dashboard dependency graph.
 */
val DashboardModule = module {
    singleOf(::DashboardConfigurationDatabaseImpl) bind DashboardConfiguration::class
}
