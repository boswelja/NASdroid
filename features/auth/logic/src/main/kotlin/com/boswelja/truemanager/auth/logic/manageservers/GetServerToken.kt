package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore

/**
 * Gets the stored authentication token for a server. See [invoke] for details.
 */
class GetServerToken(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    /**
     * Gets the stored token for the server with the given [Server.id].
     */
    suspend operator fun invoke(id: String): Result<String> = runCatching {
        authenticatedServersStore.get(id).token
    }
}
