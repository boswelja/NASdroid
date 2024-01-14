package com.nasdroid.auth.data.serverstore

import com.nasdroid.auth.data.Server
import kotlinx.coroutines.flow.Flow

/**
 * A repository for storing servers that we have previously authenticated with. Note that an entry
 * stored here doesn't mean we are still authenticated, since the token may have been revoked or
 * expired.
 */
interface AuthenticatedServersStore {

    /**
     * Flow a list of all [Server]s.
     */
    fun getAllServers(): Flow<List<Server>>

    /**
     * Get authentication information for a server by its [Server.uid].
     */
    suspend fun getAuthentication(serverId: String): Authentication

    /**
     * Delete an [Server] from the store.
     */
    suspend fun delete(server: Server)

    /**
     * Add a new server to the authentication store.
     */
    suspend fun add(server: Server, authentication: Authentication)
}

/**
 * Describes a mode of authentication for a server.
 */
sealed interface Authentication {

    /**
     * Authentication via an API key.
     *
     * @property key The API key to use for authentication.
     */
    data class ApiKey(
        val key: String
    ) : Authentication

    /**
     * Describes authentication via a username and password.
     *
     * @property username The username to use.
     * @property password The password to use.
     */
    data class Basic(
        val username: String,
        val password: String
    ) : Authentication
}
