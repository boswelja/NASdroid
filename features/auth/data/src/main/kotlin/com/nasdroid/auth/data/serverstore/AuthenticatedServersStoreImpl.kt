package com.nasdroid.auth.data.serverstore

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nasdroid.auth.data.serverstore.sqldelight.AuthDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class AuthenticatedServersStoreImpl(
    private val database: AuthDatabase,
) : AuthenticatedServersStore {

    private val context: CoroutineContext = Dispatchers.IO

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAll(): Flow<List<AuthenticatedServer>> {
        return database.storedServersQueries.selectAll().asFlow()
            .mapToList(context)
            .mapLatest { servers ->
                servers.map { server ->
                    if (server.key != null) {
                        AuthenticatedServer.ApiKey(
                            uid = server.server_id,
                            serverAddress = server.url,
                            name = server.name,
                            apiKey = server.key,
                        )
                    } else {
                        AuthenticatedServer.Basic(
                            uid = server.server_id,
                            serverAddress = server.url,
                            name = server.name,
                            username = server.username!!,
                            password = server.password!!
                        )
                    }
                }
            }
    }

    override suspend fun get(id: String): AuthenticatedServer {
        return withContext(context) {
            val dto = database.storedServersQueries.selectOne(id).executeAsOne()
            if (dto.key != null) {
                AuthenticatedServer.ApiKey(
                    uid = dto.server_id,
                    serverAddress = dto.url,
                    name = dto.name,
                    apiKey = dto.key,
                )
            } else {
                AuthenticatedServer.Basic(
                    uid = dto.server_id,
                    serverAddress = dto.url,
                    name = dto.name,
                    username = dto.username!!,
                    password = dto.password!!
                )
            }
        }
    }

    override suspend fun delete(server: AuthenticatedServer) {
        withContext(context) {
            database.storedServersQueries.delete(server.uid)
        }
    }

    override suspend fun add(server: AuthenticatedServer) {
        withContext(context) {
            database.transaction {
                when (server) {
                    is AuthenticatedServer.ApiKey -> {
                        database.apiKeysQueries.insert(
                            key_id = null,
                            key = server.apiKey,
                        )
                        val keyId = database.apiKeysQueries.lastInsertedRowId().executeAsOne()
                        database.storedServersQueries.insert(
                            server_id = server.uid,
                            name = server.name,
                            url = server.serverAddress,
                            basic_auth_id = null,
                            api_key_id = keyId
                        )
                    }
                    is AuthenticatedServer.Basic -> {
                        database.basicAuthsQueries.insert(
                            basic_id = null,
                            username = server.username,
                            password = server.password,
                        )
                        val keyId = database.basicAuthsQueries.lastInsertedRowId().executeAsOne()
                        database.storedServersQueries.insert(
                            server_id = server.uid,
                            name = server.name,
                            url = server.serverAddress,
                            basic_auth_id = keyId,
                            api_key_id = null
                        )
                    }
                }
            }
        }
    }
}
