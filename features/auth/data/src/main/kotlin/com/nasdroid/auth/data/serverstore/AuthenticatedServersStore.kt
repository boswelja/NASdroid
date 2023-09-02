package com.nasdroid.auth.data.serverstore

import kotlinx.coroutines.flow.Flow

/**
 * A repository for storing servers that we have previously authenticated with. Note that an entry
 * stored here doesn't mean we are still authenticated, since the token may have been revoked or
 * expired.
 */
interface AuthenticatedServersStore {

    /**
     * Flow a list of all [AuthenticatedServer]s.
     */
    fun getAll(): Flow<List<AuthenticatedServer>>

    /**
     * Get an authenticated server details by its [AuthenticatedServer.uid].
     */
    suspend fun get(id: String): AuthenticatedServer

    /**
     * Delete an [AuthenticatedServer] from the store.
     */
    suspend fun delete(server: AuthenticatedServer)

    /**
     * Add a new server to the authentication store.
     */
    suspend fun add(server: AuthenticatedServer)
}

/**
 * Details for a server that has been previously authenticated.
 * @property uid A unique identifier for this server.
 * @property serverAddress The URL of the server. Note this is different from the URL for the API.
 * @property token The bearer token used to access the server. This may or may not still be valid.
 * @property name A friendly name for the server.
 */
data class AuthenticatedServer(
    val uid: String,
    val serverAddress: String,
    val token: String,
    val name: String,
)
