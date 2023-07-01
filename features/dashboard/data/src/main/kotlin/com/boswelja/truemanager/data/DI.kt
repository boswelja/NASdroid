package com.boswelja.truemanager.data

import com.boswelja.truemanager.business.configuration.DashboardConfiguration
import com.boswelja.truemanager.business.configuration.database.DashboardConfigurationDatabaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardBusinessModule = module {
    singleOf(::DashboardConfigurationDatabaseImpl) bind DashboardConfiguration::class
}
