package com.boswelja.truemanager.data

import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.database.DashboardConfigurationDatabaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dashboardDataModule = module {
    singleOf(::DashboardConfigurationDatabaseImpl) bind DashboardConfiguration::class
}
