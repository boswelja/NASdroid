package com.nasdroid.auth.logic.manageservers

import com.nasdroid.auth.data.serverstore.AuthenticatedServer
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore

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
                token = token,
                name = server.name
            )
        )
    }
}
