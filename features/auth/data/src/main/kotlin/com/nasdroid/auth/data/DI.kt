package com.nasdroid.auth.data

import com.nasdroid.auth.data.currentserver.CurrentServerSource
import com.nasdroid.auth.data.currentserver.InMemoryCurrentServerSource
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStoreImpl
import com.nasdroid.auth.data.serverstore.sqldelight.createAuthDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the auth data dependency graph.
 */
val AuthDataModule = module {
    single { createAuthDatabase(androidApplication()) }
    singleOf(::AuthenticatedServersStoreImpl) bind AuthenticatedServersStore::class
    singleOf(::InMemoryCurrentServerSource) bind CurrentServerSource::class
}
