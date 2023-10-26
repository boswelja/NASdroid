package com.nasdroid.auth.ui

import com.nasdroid.auth.logic.AuthLogicModule
import com.nasdroid.auth.ui.register.auth.AuthServerViewModel
import com.nasdroid.auth.ui.serverselect.SelectServerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the auth dependency graph.
 */
val AuthModule = module {
    loadKoinModules(AuthLogicModule)

    viewModelOf(::AuthServerViewModel)
    viewModelOf(::SelectServerViewModel)
}
