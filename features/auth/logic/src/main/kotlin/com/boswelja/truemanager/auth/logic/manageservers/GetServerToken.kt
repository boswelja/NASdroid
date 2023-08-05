package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore

class GetServerToken(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    suspend operator fun invoke(id: String): Result<String> = runCatching {
        authenticatedServersStore.get(id).token
    }
}
