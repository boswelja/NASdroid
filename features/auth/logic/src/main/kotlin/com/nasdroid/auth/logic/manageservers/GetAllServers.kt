package com.nasdroid.auth.logic.manageservers

import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.logic.Server
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Retrieve all stored servers. See [invoke] for details.
 */
class GetAllServers(
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    /**
     * Get a [Flow] of all [Server]s that are stored.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<Server>> {
        return authenticatedServersStore.getAllServers()
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
