package com.boswelja.truemanager.dashboard.business

import com.boswelja.truemanager.data.dashboardDataModule
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

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