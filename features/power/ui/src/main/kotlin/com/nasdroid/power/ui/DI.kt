package com.nasdroid.power.ui

import com.nasdroid.power.logic.PowerLogicModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the power dependency graph.
 */
val PowerModule = module {
    includes(PowerLogicModule)

    viewModelOf(::PowerOptionsViewModel)
}
