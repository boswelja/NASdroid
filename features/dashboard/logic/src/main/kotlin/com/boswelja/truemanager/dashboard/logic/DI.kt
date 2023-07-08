package com.boswelja.truemanager.dashboard.logic

import com.boswelja.truemanager.dashboard.logic.configuration.InitializeDashboard
import com.boswelja.truemanager.dashboard.logic.dataloading.ExtractCpuUsageData
import com.boswelja.truemanager.dashboard.logic.dataloading.ExtractDashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.ExtractMemoryUsageData
import com.boswelja.truemanager.dashboard.logic.dataloading.ExtractNetworkUsageData
import com.boswelja.truemanager.dashboard.logic.dataloading.ExtractSystemInformationData
import com.boswelja.truemanager.dashboard.logic.dataloading.GetDashboardData
import com.boswelja.truemanager.dashboard.logic.dataloading.GetReportingDataForEntries
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
