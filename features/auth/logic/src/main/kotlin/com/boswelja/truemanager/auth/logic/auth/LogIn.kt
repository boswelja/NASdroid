package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.auth.logic.TestApiKey
import com.boswelja.truemanager.auth.logic.manageservers.GetServerToken
import com.boswelja.truemanager.auth.logic.manageservers.Server
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization

class LogIn(
    private val apiStateProvider: ApiStateProvider,
    private val testApiKey: TestApiKey,
    private val getServerToken: GetServerToken,
) {

    suspend operator fun invoke(server: Server) : Result<Unit> =
        getServerToken(server.id)
            .onSuccess { testApiKey(server.url, it) }
            .onSuccess {
                apiStateProvider.serverAddress = server.url
                apiStateProvider.authorization = Authorization.ApiKey(it)
            }
            .map { } // Remove the token from the result data
}
