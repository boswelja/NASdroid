package com.boswelja.truemanager.auth.serverstore

import kotlinx.coroutines.flow.Flow

interface AuthenticatedServersStore {

    fun getAll(): Flow<List<AuthenticatedServer>>

    suspend fun delete(server: AuthenticatedServer)

    suspend fun add(serverAddress: String, token: String)
}

data class AuthenticatedServer(
    val serverAddress: String,
    val token: String,
)
