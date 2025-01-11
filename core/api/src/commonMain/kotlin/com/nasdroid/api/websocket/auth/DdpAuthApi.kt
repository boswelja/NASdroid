package com.nasdroid.api.websocket.auth

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.callMethod
import kotlin.time.Duration

class DdpAuthApi(private val client: DdpWebsocketClient) : AuthApi {
    override suspend fun checkPassword(username: String, password: String): Boolean {
        return client.callMethod<Boolean, String>(
            "auth.check_password",
            listOf(username, password),
        )
    }

    override suspend fun checkUser(username: String, password: String): Boolean {
        return client.callMethod<Boolean, String>(
            "auth.check_user",
            listOf(username, password),
        )
    }

    override suspend fun generateToken(
        timeToLive: Duration,
        attrs: Map<String, Any>,
        matchOrigin: Boolean
    ): String {
        return client.callMethod(
            "auth.generate_token",
            listOf(TimeToLive(timeToLive.inWholeSeconds), TokenAttributes(attrs), MatchOrigin(matchOrigin)),
        )
    }

    override suspend fun logIn(username: String, password: String, otpToken: String?): Boolean {
        return client.callMethod("auth.login", listOf(username, password, otpToken))
    }

    override suspend fun logInWithApiKey(apiKey: String): Boolean {
        return client.callMethod("auth.login_with_api_key", listOf(apiKey),)
    }

    override suspend fun logInWithToken(token: String): Boolean {
        return client.callMethod("auth.login_with_token", listOf(token))
    }

    override suspend fun logOut(): Boolean {
        return client.callMethod("auth.logout")
    }

    override suspend fun me(): AuthenticatedUser {
        return client.callMethod("auth.me")
    }

    override suspend fun sessions(): List<Session> {
        return client.callMethod("auth.sessions")
    }

    override suspend fun setAttribute(key: String, value: String) {
        return client.callMethod("auth.set_attribute", listOf(key, value))
    }

    override suspend fun terminateOtherSessions() {
        return client.callMethod("auth.terminate_other_sessions")
    }

    override suspend fun terminateSession(sessionId: String): Boolean {
        return client.callMethod("auth.terminate_session", listOf(sessionId))
    }

    override suspend fun twoFactorAuth(username: String, password: String): Boolean {
        return client.callMethod<Boolean, String>("auth.two_factor_auth", listOf(username, password))
    }
}