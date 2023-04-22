package com.boswelja.truemanager.auth.serverstore

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
     * Delete an [AuthenticatedServer] from the store.
     */
    suspend fun delete(server: AuthenticatedServer)

    /**
     * Add a new server to the authentication store.
     */
    suspend fun add(serverAddress: String, token: String)
}

/**
 * Details for a server that has been previously authenticated.
 * @property serverAddress The URL of the server. Note this is different from the URL for the API.
 * @property token The bearer token used to access the server. This may or may not still be valid.
 */
data class AuthenticatedServer(
    val serverAddress: String,
    val token: String,
)
