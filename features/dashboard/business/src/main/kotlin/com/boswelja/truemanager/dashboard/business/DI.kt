package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.data.dashboardDataModule
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module that defines bindings for business classes. This should be loaded when a dependency
 * on business exists.
 */
val dashboardBusinessModule = module {
    loadKoinModules(dashboardDataModule)

    factoryOf(::ExtractCpuUsageData)
    factoryOf(::ExtractDashboardData)
    factoryOf(::ExtractMemoryUsageData)
    factoryOf(::ExtractNetworkUsageData)
    factoryOf(::ExtractSystemInformationData)
    factoryOf(::GetDashboardData)
    factoryOf(::GetReportingDataForEntries)
    factoryOf(::InitializeDashboard)
}
