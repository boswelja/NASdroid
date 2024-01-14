package com.nasdroid.auth.data.serverstore

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nasdroid.auth.data.Server
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
    override fun getAllServers(): Flow<List<Server>> {
        return database.storedServersQueries.selectAll().asFlow()
            .mapToList(context)
            .mapLatest { servers ->
                servers.map { server ->
                    Server(
                        server.server_id,
                        server.url,
                        server.name,
                    )
                }
            }
    }

    override suspend fun getAuthentication(serverId: String): Authentication {
        return withContext(context) {
            val authData = database.storedServersQueries.selectAuthentication(serverId)
                .executeAsOne()
            if (authData.key != null) {
                Authentication.ApiKey(authData.key)
            } else {
                requireNotNull(authData.username)
                requireNotNull(authData.password)
                Authentication.Basic(authData.username, authData.password)
            }
        }
    }

    override suspend fun delete(server: Server) {
        withContext(context) {
            database.storedServersQueries.delete(server.uid)
        }
    }

    override suspend fun add(server: Server, authentication: Authentication) {
        withContext(context) {
            database.transaction {
                when (authentication) {
                    is Authentication.ApiKey -> {
                        database.apiKeysQueries.insert(
                            key_id = null,
                            key = authentication.key,
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
                    is Authentication.Basic -> {
                        database.basicAuthsQueries.insert(
                            basic_id = null,
                            username = authentication.username,
                            password = authentication.password,
                        )
                        val basicId = database.basicAuthsQueries.lastInsertedRowId().executeAsOne()
                        database.storedServersQueries.insert(
                            server_id = server.uid,
                            name = server.name,
                            url = server.serverAddress,
                            basic_auth_id = basicId,
                            api_key_id = null
                        )
                    }
                }
            }
        }
    }
}
