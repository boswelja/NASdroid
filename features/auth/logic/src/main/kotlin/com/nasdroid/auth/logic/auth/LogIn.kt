package com.nasdroid.auth.logic.auth

import com.nasdroid.api.v2.ApiStateProvider
import com.nasdroid.api.v2.Authorization
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.auth.logic.Server

/**
 * Attempts to authenticate with a server. See [invoke] for details.
 */
class LogIn(
    private val apiStateProvider: ApiStateProvider,
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    /**
     * Attempts to authenticate with the given [Server]. If the stored token does not work, an error
     * is returned.
     */
    suspend operator fun invoke(server: Server) : Result<Unit> = runCatching {
        val authentication = authenticatedServersStore.getAuthentication(server.id)
        apiStateProvider.serverAddress = server.url
        apiStateProvider.authorization = when (authentication) {
            is Authentication.ApiKey -> Authorization.ApiKey(authentication.key)
            is Authentication.Basic -> Authorization.Basic(authentication.username, authentication.password)
        }
    }
}
