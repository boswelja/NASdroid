package com.nasdroid.auth.logic.auth

import com.nasdroid.auth.logic.TestServerToken
import com.nasdroid.auth.logic.manageservers.GetServerToken
import com.nasdroid.auth.logic.manageservers.Server

/**
 * Attempts to authenticate with a server. See [invoke] for details.
 */
class LogIn(
    private val apiStateProvider: com.nasdroid.api.v2.ApiStateProvider,
    private val testServerToken: TestServerToken,
    private val getServerToken: GetServerToken,
) {

    /**
     * Attempts to authenticate with the given [Server]. If the stored token does not work, an error
     * is returned. See [TestServerToken] for key testing criteria.
     */
    suspend operator fun invoke(server: Server) : Result<Unit> =
        getServerToken(server.id)
            .mapCatching {
                testServerToken(server.url, it).getOrThrow()
                it
            }
            .onSuccess {
                apiStateProvider.serverAddress = server.url
                apiStateProvider.authorization = com.nasdroid.api.v2.Authorization.ApiKey(it)
            }
            .map { } // Remove the token from the result data
}
