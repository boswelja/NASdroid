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
                    AuthenticatedServer(
                        server.server_id,
                        server.url,
                        server.key,
                        server.name,
                    )
                }
            }
    }

    override suspend fun get(id: String): AuthenticatedServer {
        return withContext(context) {
            val dto = database.storedServersQueries.selectOne(id).executeAsOne()
            AuthenticatedServer(
                uid = dto.server_id,
                serverAddress = dto.url,
                token = dto.key,
                name = dto.name
            )
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
                database.apiKeysQueries.insert(
                    key_id = null,
                    key = server.token
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
        }
    }
}
