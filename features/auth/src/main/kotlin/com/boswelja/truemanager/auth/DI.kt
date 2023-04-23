package com.boswelja.truemanager.auth

import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStoreImpl
import com.boswelja.truemanager.auth.ui.addserver.AddServerViewModel
import com.boswelja.truemanager.auth.ui.serverselect.SelectServerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the auth dependency graph. This depends on the API module.
 */
val AuthModule = module {
    singleOf(::AuthenticatedServersStoreImpl) bind AuthenticatedServersStore::class

    viewModelOf(::AddServerViewModel)
    viewModelOf(::SelectServerViewModel)
}
