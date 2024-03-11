package com.nasdroid.power.logic

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the power logic dependency graph.
 */
val PowerLogicModule = module {
    factoryOf(::ShutdownSystem)
    factoryOf(::RebootSystem)
}
