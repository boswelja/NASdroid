package com.nasdroid.power.ui

import com.nasdroid.power.logic.PowerLogicModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val PowerModule = module {
    includes(PowerLogicModule)

    viewModelOf(::PowerOptionsViewModel)
}
