package com.nasdroid.auth.logic.manageservers

import com.nasdroid.auth.data.serverstore.Server as AuthenticatedServer
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.auth.logic.Server

/**
 * Adds a new server to the authenticated server store. See [invoke] for details.
 */
class StoreNewServer(
    private val authedServersStore: AuthenticatedServersStore,
) {

    /**
     * Adds the given [Server] and [token] to the authenticated server store.
     */
    suspend operator fun invoke(
        server: Server,
        token: String
    ): Result<Unit> = runCatching {
        authedServersStore.add(
            AuthenticatedServer(
                uid = server.id,
                serverAddress = server.url,
                name = server.name
            ),
            Authentication.ApiKey(token)
        )
    }
}
