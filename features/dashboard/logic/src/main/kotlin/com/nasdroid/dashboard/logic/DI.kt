package com.nasdroid.dashboard.logic

import com.nasdroid.dashboard.logic.configuration.GetDashboardItems
import com.nasdroid.dashboard.logic.configuration.InitializeDashboard
import com.nasdroid.dashboard.logic.configuration.ReorderDashboardItems
import com.nasdroid.dashboard.logic.configuration.SaveDashboardOrder
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuUsageData
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemorySpecs
import com.nasdroid.dashboard.logic.dataloading.memory.GetMemoryUsageData
import com.nasdroid.dashboard.logic.dataloading.network.GetNetworkConfiguration
import com.nasdroid.dashboard.logic.dataloading.network.GetNetworkUsageData
import com.nasdroid.dashboard.logic.dataloading.system.GetSystemInformation
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

    factoryOf(::GetNetworkConfiguration)
    factoryOf(::GetNetworkUsageData)

    factoryOf(::GetSystemInformation)

    factoryOf(::GetDashboardItems)
    factoryOf(::InitializeDashboard)
    factoryOf(::ReorderDashboardItems)
    factoryOf(::SaveDashboardOrder)
}
