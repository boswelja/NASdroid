package com.boswelja.truemanager.auth.logic

import com.boswelja.truemanager.auth.data.AuthDataModule
import com.boswelja.truemanager.auth.logic.auth.LogIn
import com.boswelja.truemanager.auth.logic.auth.LogOut
import com.boswelja.truemanager.auth.logic.manageservers.AddNewServer
import com.boswelja.truemanager.auth.logic.manageservers.AuthenticateAndAddServer
import com.boswelja.truemanager.auth.logic.manageservers.GetAllServers
import com.boswelja.truemanager.auth.logic.manageservers.GetServerToken
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
    factoryOf(::AuthenticateAndAddServer)
    factoryOf(::GetAllServers)
    factoryOf(::GetServerToken)

    factoryOf(::CreateApiKey)
    factoryOf(::TestApiKey)
}
