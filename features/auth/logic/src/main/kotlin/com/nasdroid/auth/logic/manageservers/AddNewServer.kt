package com.nasdroid.auth.logic.manageservers

import com.nasdroid.auth.logic.then
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.auth.data.serverstore.Server
import com.nasdroid.auth.logic.auth.TestServerAuthentication

/**
 * Creates a token for and stores a new server. See [invoke] for details.
 */
class AddNewServer(
    private val systemV2Api: SystemV2Api,
    private val testServerAuthentication: TestServerAuthentication,
    private val authenticatedServersStore: AuthenticatedServersStore,
) {

    /**
     * Adds a server with the given [username] and [password] combination.
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String
    ): Result<Unit> =
        testServerAuthentication(serverAddress, username, password)
            .then {
                storeNewServer(
                    serverName = serverName,
                    serverAddress = serverAddress,
                    authentication = Authentication.Basic(username, password)
                )
            }

    /**
     * Adds a server with the given [token].
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        token: String
    ): Result<Unit> =
        testServerAuthentication(serverAddress, token)
            .then {
                storeNewServer(
                    serverName = serverName,
                    serverAddress = serverAddress,
                    Authentication.ApiKey(token)
                )
            }

    private suspend fun storeNewServer(
        serverName: String,
        serverAddress: String,
        authentication: Authentication,
    ): Result<Unit> = runCatching {
        val actualName = serverName.ifBlank {
            val systemInfo = systemV2Api.getSystemInfo()
            systemInfo.systemProduct
        }
        val uid = systemV2Api.getHostId()
        authenticatedServersStore.add(
            Server(uid = uid, serverAddress = serverAddress, name = actualName),
            authentication
        )
    }
}
