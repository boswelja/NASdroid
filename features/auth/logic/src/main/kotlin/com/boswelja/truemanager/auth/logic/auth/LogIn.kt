package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.logic.TestApiKey
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization

class LogIn(
    private val apiStateProvider: ApiStateProvider,
    private val testApiKey: TestApiKey,
) {

    suspend operator fun invoke(server: AuthenticatedServer) : Result<Unit> =
        testApiKey(server.serverAddress, server.token)
            .onSuccess {
                apiStateProvider.serverAddress = server.serverAddress
                apiStateProvider.authorization = Authorization.ApiKey(server.token)
            }
}
