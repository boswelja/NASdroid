package com.nasdroid.power.logic

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val PowerLogicModule = module {
    factoryOf(::ShutdownSystem)
    factoryOf(::RebootSystem)
}
