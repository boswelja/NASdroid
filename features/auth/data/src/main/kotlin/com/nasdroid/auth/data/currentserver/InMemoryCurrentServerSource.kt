package com.nasdroid.auth.data.currentserver

import com.nasdroid.auth.data.Server
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * An implementation of [CurrentServerSource] that stores the server in-memory.
 */
class InMemoryCurrentServerSource : CurrentServerSource {
    private val currentServer = MutableStateFlow<Server?>(null)
    override fun getCurrentServer(): Flow<Server?> = currentServer

    override fun setCurrentServer(server: Server?) {
        currentServer.value = server
    }
}