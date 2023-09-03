package com.nasdroid.auth.data.serverstore

import android.content.Context
import androidx.room.Room
import com.nasdroid.auth.data.serverstore.room.AuthenticatedServerDatabase
import com.nasdroid.auth.data.serverstore.room.AuthenticatedServerDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

internal class AuthenticatedServersStoreImpl(
    context: Context
) : AuthenticatedServersStore {

    private val database = Room.databaseBuilder(
        context,
        AuthenticatedServerDatabase::class.java,
        "authenticated-servers"
    ).build()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<AuthenticatedServer>> {
        return database.authenticatedServerDao().getAll().mapLatest { servers ->
            servers.map { server ->
                AuthenticatedServer(
                    server.hostId,
                    server.serverAddress,
                    server.token,
                    server.name,
                )
            }
        }
    }

    override suspend fun get(id: String): AuthenticatedServer {
        val dto = database.authenticatedServerDao().get(id)
        return AuthenticatedServer(
            uid = dto.hostId,
            serverAddress = dto.serverAddress,
            token = dto.token,
            name = dto.name
        )
    }

    override suspend fun delete(server: AuthenticatedServer) {
        database.authenticatedServerDao().delete(
            AuthenticatedServerDto(
                server.uid,
                server.serverAddress,
                server.token,
                server.name,
            )
        )
    }

    override suspend fun add(server: AuthenticatedServer) {
        database.authenticatedServerDao().insertAll(
            AuthenticatedServerDto(
                server.uid,
                server.serverAddress,
                server.token,
                server.name,
            )
        )
    }
}
