package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServer
import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore
import kotlinx.coroutines.flow.Flow

class GetAllServers(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    operator fun invoke(): Flow<List<AuthenticatedServer>> {
        return authenticatedServersStore.getAll()
    }
}
