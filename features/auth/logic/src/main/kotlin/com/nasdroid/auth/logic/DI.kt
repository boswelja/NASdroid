package com.nasdroid.auth.logic

import com.nasdroid.auth.data.AuthDataModule
import com.nasdroid.auth.logic.auth.LogIn
import com.nasdroid.auth.logic.auth.LogOut
import com.nasdroid.auth.logic.internal.CreateApiKey
import com.nasdroid.auth.logic.manageservers.StoreNewServer
import com.nasdroid.auth.logic.manageservers.AddNewServer
import com.nasdroid.auth.logic.manageservers.GetAllServers
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the auth logic dependency graph.
 */
val AuthLogicModule = module {
    loadKoinModules(AuthDataModule)

    factoryOf(::LogIn)
    factoryOf(::LogOut)

    factoryOf(::AddNewServer)
    factoryOf(::GetAllServers)
    factoryOf(::StoreNewServer)

    factoryOf(::CreateApiKey)
}
