package com.boswelja.truemanager.auth.ui

import com.boswelja.truemanager.auth.data.AuthDataModule
import com.boswelja.truemanager.auth.ui.addserver.AddServerViewModel
import com.boswelja.truemanager.auth.ui.serverselect.SelectServerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the auth dependency graph.
 */
val AuthModule = module {
    loadKoinModules(AuthDataModule)

    viewModelOf(::AddServerViewModel)
    viewModelOf(::SelectServerViewModel)
}
