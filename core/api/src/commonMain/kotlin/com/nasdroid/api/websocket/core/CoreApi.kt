package com.nasdroid.api.websocket.core

interface CoreApi {

    /**
     * -
     */
    suspend fun arp(ip: String? = null, `interface`: String? = null)

    // TODO bulk?

    /**
     * Setup middlewared for remote debugging.
     *
     * engine currently used: - REMOTE_PDB: Remote vanilla PDB (over TCP sockets)
     *
     * @param bindAddress: local ip address to bind the remote debug session to
     * @param bindPort: local port to listen on
     * @param threaded: run debugger in a new thread instead of the main event loop
     */
    suspend fun debug(bindAddress: String = "0.0.0.0", bindPort: Int = 3000, threaded: Boolean = false)

    /**
     * -
     */
    suspend fun debugModeEnabled(): Boolean

    // TODO download
}