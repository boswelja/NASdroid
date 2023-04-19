package com.boswelja.truemanager.auth

import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.auth.serverstore.AuthenticatedServersStoreImpl
import com.boswelja.truemanager.auth.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthenticatedServersStoreImpl) bind AuthenticatedServersStore::class

    viewModelOf(::AuthViewModel)
}
