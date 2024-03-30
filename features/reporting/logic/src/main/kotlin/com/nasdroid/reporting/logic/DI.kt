package com.nasdroid.reporting.logic

import com.nasdroid.reporting.logic.graph.GetCpuGraphs
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val ReportingLogicModule = module {
    factoryOf(::GetCpuGraphs)
}
