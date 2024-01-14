package com.nasdroid.auth.data.currentserver

import com.nasdroid.auth.data.Server
import kotlinx.coroutines.flow.Flow

/**
 * Tracks the currently connected server, i.e. the server that we are making network requests to.
 */
interface CurrentServerSource {

    /**
     * Flows the currently connected server, or null if no server is connected.
     */
    fun getCurrentServer(): Flow<Server?>

    /**
     * Sets the currently connected server, or null if no server is connected.
     */
    fun setCurrentServer(server: Server?)
}
