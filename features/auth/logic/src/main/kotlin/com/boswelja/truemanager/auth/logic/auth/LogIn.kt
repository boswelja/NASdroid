package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.auth.logic.TestServerToken
import com.boswelja.truemanager.auth.logic.manageservers.GetServerToken
import com.boswelja.truemanager.auth.logic.manageservers.Server
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization

/**
 * Attempts to authenticate with a server. See [invoke] for details.
 */
class LogIn(
    private val apiStateProvider: ApiStateProvider,
    private val testServerToken: TestServerToken,
    private val getServerToken: GetServerToken,
) {

    /**
     * Attempts to authenticate with the given [Server]. If the stored token does not work, an error
     * is returned. See [TestServerToken] for key testing criteria.
     */
    suspend operator fun invoke(server: Server) : Result<Unit> =
        getServerToken(server.id)
            .onSuccess { testServerToken(server.url, it) }
            .onSuccess {
                apiStateProvider.serverAddress = server.url
                apiStateProvider.authorization = Authorization.ApiKey(it)
            }
            .map { } // Remove the token from the result data
}
