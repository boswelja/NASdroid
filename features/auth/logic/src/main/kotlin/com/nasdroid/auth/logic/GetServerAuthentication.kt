package com.nasdroid.auth.logic

import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore

class GetServerAuthentication(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    suspend operator fun invoke(serverId: String): Result<ServerAuthentication> = runCatching {
        ServerAuthentication.ApiKey(authenticatedServersStore.get(serverId).token)
    }
}

sealed interface ServerAuthentication {

    data class ApiKey(val key: String) : ServerAuthentication
}
