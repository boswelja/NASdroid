package com.nasdroid.reporting.logic

import com.nasdroid.reporting.logic.graph.GetCpuGraphs
import com.nasdroid.reporting.logic.graph.GetMemoryGraphs
import com.nasdroid.reporting.logic.graph.GetSystemGraphs
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the reporting logic dependency graph.
 */
val ReportingLogicModule = module {
    factoryOf(::GetCpuGraphs)
    factoryOf(::GetMemoryGraphs)
    factoryOf(::GetSystemGraphs)
}
