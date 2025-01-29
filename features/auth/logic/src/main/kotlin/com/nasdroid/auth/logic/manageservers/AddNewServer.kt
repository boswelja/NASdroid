package com.nasdroid.auth.logic.manageservers

import com.nasdroid.api.v2.ApiStateProvider
import com.nasdroid.api.v2.Authorization
import com.nasdroid.api.v2.exception.ClientUnauthorizedException
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.auth.data.Server
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.core.strongresult.StrongResult
import java.net.UnknownHostException

/**
 * Creates a token for and stores a new server. See [invoke] for details.
 */
class AddNewServer(
    private val systemV2Api: SystemV2Api,
    private val authenticatedServersStore: AuthenticatedServersStore,
    private val apiStateProvider: ApiStateProvider,
) {

    /**
     * Adds a server with the given [username] and [password] combination.
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String
    ): StrongResult<Unit, AddServerError> {
        return try {
            apiStateProvider.serverAddress = serverAddress
            apiStateProvider.authorization = Authorization.Basic(username, password)
            storeNewServer(
                serverName = serverName,
                serverAddress = serverAddress,
                authentication = Authentication.Basic(username, password)
            )
        } finally {
            apiStateProvider.serverAddress = null
            apiStateProvider.authorization = null
        }
    }

    /**
     * Adds a server with the given [token].
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        token: String
    ): StrongResult<Unit, AddServerError> {
        return try {
            apiStateProvider.serverAddress = serverAddress
            apiStateProvider.authorization = Authorization.ApiKey(token)
            storeNewServer(
                serverName = serverName,
                serverAddress = serverAddress,
                authentication = Authentication.ApiKey(token)
            )
        } finally {
            apiStateProvider.serverAddress = null
            apiStateProvider.authorization = null
        }
    }

    private suspend fun storeNewServer(
        serverName: String,
        serverAddress: String,
        authentication: Authentication,
    ): StrongResult<Unit, AddServerError> {
        return try {
            val actualName = serverName.ifBlank {
                val systemInfo = systemV2Api.getSystemInfo()
                systemInfo.hostName
            }
            val uid = systemV2Api.getHostId()
            authenticatedServersStore.add(
                Server(uid = uid, serverAddress = serverAddress, name = actualName),
                authentication
            )
            StrongResult.success(Unit)
        } catch (_: ClientUnauthorizedException) {
            StrongResult.failure(AddServerError.InvalidCredentials)
        } catch (_: UnknownHostException) {
            StrongResult.failure(AddServerError.ServerNotFound)
        }
    }
}

/**
 * Encapsulates all possible error states for [AddNewServer].
 */
sealed interface AddServerError {

    /**
     * Indicates that a server could not be found at the address provided. THis could be caused by
     * an incorrect address, or no internet.
     */
    data object ServerNotFound : AddServerError

    /**
     * Indicates that the server was reached, but the credentials were not valid. This could be
     * caused by the username, password, or token being incorrect.
     */
    data object InvalidCredentials : AddServerError

    /**
     * Indicates that there is already a server that is registered with the same data.
     */
    data object DuplicateEntry : AddServerError
}
