package com.nasdroid.auth.logic.manageservers

import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore

/**
 * Gets the stored authentication token for a server. See [invoke] for details.
 */
@Deprecated("Use GetServerAuthentication instead")
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
