package com.nasdroid.reporting.logic

import com.nasdroid.reporting.logic.graph.GetCpuGraphs
import com.nasdroid.reporting.logic.graph.GetMemoryGraphs
import com.nasdroid.reporting.logic.graph.GetNetworkGraphs
import com.nasdroid.reporting.logic.graph.GetNetworkInterfaces
import com.nasdroid.reporting.logic.graph.GetSystemGraphs
import com.nasdroid.reporting.logic.graph.GetZfsGraphs
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the reporting logic dependency graph.
 */
val ReportingLogicModule = module {
    factoryOf(::GetCpuGraphs)
    factoryOf(::GetMemoryGraphs)
    factoryOf(::GetNetworkGraphs)
    factoryOf(::GetNetworkInterfaces)
    factoryOf(::GetSystemGraphs)
    factoryOf(::GetZfsGraphs)
}
