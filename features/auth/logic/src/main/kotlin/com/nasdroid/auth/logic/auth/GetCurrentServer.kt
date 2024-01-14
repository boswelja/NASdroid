package com.nasdroid.auth.logic.auth

import com.nasdroid.auth.data.currentserver.CurrentServerSource
import com.nasdroid.auth.logic.Server
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Get the currently connected server. See [invoke] for details.
 */
class GetCurrentServer(
    private val currentServerSource: CurrentServerSource
) {
    /**
     * Flow the currently connected server.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Server?> {
        return currentServerSource.getCurrentServer()
            .mapLatest {
                it?.let { server ->
                    Server(
                        id = server.uid,
                        name = server.name,
                        url = server.serverAddress
                    )
                }
            }
    }
}