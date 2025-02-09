package com.nasdroid.auth.logic.manageservers

import com.nasdroid.api.v2.exception.ClientUnauthorizedException
import com.nasdroid.api.websocket.auth.AuthApi
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.system.SystemApi
import com.nasdroid.auth.data.Server
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.auth.logic.auth.CreateApiKey
import com.nasdroid.auth.logic.auth.CreateApiKeyError
import com.nasdroid.auth.logic.auth.DeriveUriError
import com.nasdroid.auth.logic.auth.DeriveUriFromInput
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.core.strongresult.fold
import java.net.UnknownHostException

/**
 * Creates a token for and stores a new server. See [invoke] for details.
 */
class AddNewServer(
    private val createApiKey: CreateApiKey,
    private val deriveUriFromInput: DeriveUriFromInput,
    private val authenticatedServersStore: AuthenticatedServersStore,
    private val client: DdpWebsocketClient,
    private val authApi: AuthApi,
    private val systemApi: SystemApi,
) {

    /**
     * Adds a server with the given [username] and [password] combination.
     */
    suspend operator fun invoke(
        serverName: String,
        serverAddress: String,
        username: String,
        password: String,
        createApiKey: Boolean,
    ): StrongResult<Unit, AddServerError> {
        val targetAddress = deriveUriFromInput(serverAddress).fold(
            onSuccess = { it },
            onFailure = {
                return StrongResult.failure(AddServerError.InvalidAddress(it))
            }
        )
        return try {
            // Attempt a connection
            client.connect(targetAddress)

            // Try to create an API key, if requested
            val authorization = if (createApiKey) {
                this.createApiKey("NASdroid", username, password).fold(
                    onSuccess = {
                        Authentication.ApiKey(it)
                    },
                    onFailure = {
                        val error = when (it) {
                            CreateApiKeyError.InvalidCredentials -> AddServerError.InvalidCredentials
                        }
                        return StrongResult.failure(error)
                    }
                )
            } else {
                // If we're not making an API key, we need to manually check credentials.
                val success = authApi.logIn(username, password)
                if (!success) {
                    return StrongResult.failure(AddServerError.InvalidCredentials)
                }
                Authentication.Basic(username, password)
            }
            storeNewServer(
                serverName = serverName,
                serverAddress = targetAddress,
                authentication = authorization
            )
        } catch (_: UnknownHostException) {
            StrongResult.failure(AddServerError.ServerNotFound)
        } catch (e: IllegalStateException) {
            throw e // We don't expect this case ever at this point
        } finally {
            client.disconnect()
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
        val targetAddress = deriveUriFromInput(serverAddress).fold(
            onSuccess = { it },
            onFailure = {
                return StrongResult.failure(AddServerError.InvalidAddress(it))
            }
        )
        return try {
            client.connect(targetAddress)
            val success = authApi.logInWithApiKey(token)
            if (success) {
                storeNewServer(
                    serverName = serverName,
                    serverAddress = targetAddress,
                    authentication = Authentication.ApiKey(token)
                )
            } else {
                StrongResult.failure(AddServerError.InvalidCredentials)
            }
        } catch (_: IllegalStateException) {
            StrongResult.failure(AddServerError.ServerNotFound)
        } finally {
            client.disconnect()
        }
    }

    private suspend fun storeNewServer(
        serverName: String,
        serverAddress: String,
        authentication: Authentication,
    ): StrongResult<Unit, AddServerError> {
        return try {
            val actualName = serverName.ifBlank {
                val systemInfo = systemApi.info()
                systemInfo.hostName
            }
            val uid = systemApi.hostId()
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

    /**
     * Indicates that the server address provided by the user was not valid. See [cause] for the
     * specific reason.
     */
    data class InvalidAddress(val cause: DeriveUriError) : AddServerError
}
