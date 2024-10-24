package com.nasdroid.auth.ui

import com.nasdroid.auth.logic.AuthLogicModule
import com.nasdroid.auth.ui.register.RegisterServerViewModel
import com.nasdroid.auth.ui.selector.ServerSelectorViewModel
import com.nasdroid.auth.ui.serverselect.SelectServerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the auth dependency graph.
 */
val AuthModule = module {
    includes(AuthLogicModule)

    viewModelOf(::RegisterServerViewModel)
    viewModelOf(::ServerSelectorViewModel)
    viewModelOf(::SelectServerViewModel)
}
