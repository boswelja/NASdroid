package com.boswelja.truemanager.auth.logic.manageservers

import com.boswelja.truemanager.auth.data.serverstore.AuthenticatedServersStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class GetAllServers(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Server>> {
        return authenticatedServersStore.getAll()
            .mapLatest { servers ->
                servers.map {
                    Server(
                        id = it.uid,
                        name = it.name,
                        url = it.serverAddress
                    )
                }
            }
    }
}
