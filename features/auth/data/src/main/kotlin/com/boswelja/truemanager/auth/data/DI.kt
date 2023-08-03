package com.boswelja.truemanager.auth.data

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStoreImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the auth data dependency graph.
 */
val AuthDataModule = module {
    singleOf(::AuthenticatedServersStoreImpl) bind AuthenticatedServersStore::class
}
