package com.nasdroid.auth.logic.auth

import com.nasdroid.api.v2.exception.ClientRequestException
import com.nasdroid.api.v2.exception.ServerResponseException
import com.nasdroid.api.websocket.auth.AuthApi
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.auth.data.currentserver.CurrentServerSource
import com.nasdroid.auth.data.serverstore.AuthenticatedServersStore
import com.nasdroid.auth.data.serverstore.Authentication
import com.nasdroid.auth.logic.Server
import com.nasdroid.core.strongresult.StrongResult
import java.net.UnknownHostException

/**
 * Attempts to authenticate with a server. See [invoke] for details.
 */
class LogIn(
    private val authenticatedServersStore: AuthenticatedServersStore,
    private val currentServerSource: CurrentServerSource,
    private val client: DdpWebsocketClient,
    private val authApi: AuthApi,
) {

    /**
     * Attempts to authenticate with the given [Server]. If the stored token does not work, an error
     * is returned.
     */
    suspend operator fun invoke(server: Server) : StrongResult<Unit, LoginError> {
        val authentication = authenticatedServersStore.getAuthentication(server.id)
        return try {
            // Connect websocket
            client.connect(server.url)
            val isSuccess = when (authentication) {
                is Authentication.ApiKey -> authApi.logInWithApiKey(authentication.key)
                is Authentication.Basic -> authApi.logIn(authentication.username, authentication.password)
            }
            if (isSuccess) {
                currentServerSource.setCurrentServer(
                    com.nasdroid.auth.data.Server(
                        uid = server.id,
                        name = server.name,
                        serverAddress = server.url
                    )
                )
                StrongResult.success(Unit)
            } else {
                client.disconnect()
                StrongResult.failure(LoginError.InvalidCredentials)
            }
        } catch (_: ClientRequestException) {
            client.disconnect()
            StrongResult.failure(LoginError.InvalidCredentials)
        } catch (_: ServerResponseException) {
            client.disconnect()
            StrongResult.failure(LoginError.Unknown)
        } catch (_: UnknownHostException) {
            client.disconnect()
            StrongResult.failure(LoginError.ServerUnreachable)
        } catch (_: IllegalStateException) {
            client.disconnect()
            StrongResult.failure(LoginError.ServerUnreachable)
        }
    }
}

/**
 * Encapsulates all possible errors that can happen during the login process.
 */
sealed interface LoginError {

    /**
     * Indicates that the server could not be reached. This could be because the server is offline
     * or on a different network. This could also mean the device has no internet.
     */
    data object ServerUnreachable : LoginError

    /**
     * Indicates that the credentials used were not valid for the target server. This could be
     * because the token used was revoked, or the account password was changed.
     */
    data object InvalidCredentials : LoginError

    /**
     * Indicates an unexpected error occurred. We're not sure what could cause this.
     */
    data object Unknown : LoginError
}
