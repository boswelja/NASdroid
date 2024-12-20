package com.nasdroid.api.websocket.auth

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.MethodCallResult
import kotlin.time.Duration

class DdpAuthApi(private val client: DdpWebsocketClient) : AuthApi {
    override suspend fun checkPassword(username: String, password: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.check_password", listOf(username, password))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun checkUser(username: String, password: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.check_user", listOf(username, password))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun generateToken(
        timeToLive: Duration,
        attrs: Map<String, Any>,
        matchOrigin: Boolean
    ): String {
        val result = client.callMethod<String, GenerateTokenParams>(
            "auth.generate_token",
            listOf(TimeToLive(timeToLive.inWholeSeconds), TokenAttributes(attrs), MatchOrigin(matchOrigin))
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<String> -> result.result
        }
    }

    override suspend fun logIn(username: String, password: String, otpToken: String?): Boolean {
        val result = client.callMethod<Boolean, String?>("auth.login", listOf(username, password, otpToken))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logInWithApiKey(apiKey: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.login_with_api_key", listOf(apiKey))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logInWithToken(token: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.login_with_token", listOf(token))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logOut(): Boolean {
        return when (val result = client.callMethod<Boolean>("auth.logout")) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun me(): AuthenticatedUser {
        return when (val result = client.callMethod<AuthenticatedUser>("auth.me")) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<AuthenticatedUser> -> result.result
        }
    }

    override suspend fun sessions(): List<Session> {
        return when (val result = client.callMethod<List<Session>>("auth.sessions")) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<List<Session>> -> result.result
        }
    }

    override suspend fun setAttribute(key: String, value: String) {
        val result = client.callMethod<Unit, String>("auth.set_attribute", listOf(key, value))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Unit> -> result.result
        }
    }

    override suspend fun terminateOtherSessions() {
        val result = client.callMethod<Unit>("auth.terminate_other_sessions")
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Unit> -> result.result
        }
    }

    override suspend fun terminateSession(sessionId: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.terminate_session", listOf(sessionId))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun twoFactorAuth(username: String, password: String): Boolean {
        val result = client.callMethod<Boolean, String>("auth.two_factor_auth", listOf(username, password))
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }
}