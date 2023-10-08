package com.nasdroid.dashboard.logic

import com.nasdroid.dashboard.logic.configuration.InitializeDashboard
import com.nasdroid.dashboard.logic.configuration.ReorderDashboardData
import com.nasdroid.dashboard.logic.configuration.SaveDashboardOrder
import com.nasdroid.dashboard.logic.dataloading.ExtractCpuUsageData
import com.nasdroid.dashboard.logic.dataloading.ExtractDashboardData
import com.nasdroid.dashboard.logic.dataloading.ExtractMemoryUsageData
import com.nasdroid.dashboard.logic.dataloading.ExtractNetworkUsageData
import com.nasdroid.dashboard.logic.dataloading.ExtractSystemInformationData
import com.nasdroid.dashboard.logic.dataloading.GetDashboardData
import com.nasdroid.dashboard.logic.dataloading.GetReportingDataForEntries
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuUsageData
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemorySpecs
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemoryUsageData
import com.nasdroid.data.dashboardDataModule
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module that defines bindings for business classes. This should be loaded when a dependency
 * on business exists.
 */
val dashboardBusinessModule = module {
    loadKoinModules(dashboardDataModule)

    factoryOf(::GetCpuSpecs)
    factoryOf(::GetCpuUsageData)

    factoryOf(::GetMemorySpecs)
    factoryOf(::GetMemoryUsageData)

    factoryOf(::ExtractCpuUsageData)
    factoryOf(::ExtractDashboardData)
    factoryOf(::ExtractMemoryUsageData)
    factoryOf(::ExtractNetworkUsageData)
    factoryOf(::ExtractSystemInformationData)
    factoryOf(::GetDashboardData)
    factoryOf(::GetReportingDataForEntries)

    factoryOf(::InitializeDashboard)
    factoryOf(::ReorderDashboardData)
    factoryOf(::SaveDashboardOrder)
}
