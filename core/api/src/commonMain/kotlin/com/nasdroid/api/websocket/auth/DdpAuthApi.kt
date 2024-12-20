package com.nasdroid.api.websocket.auth

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.MethodCallResult
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlin.time.Duration

class DdpAuthApi(private val client: DdpWebsocketClient) : AuthApi {
    override suspend fun checkPassword(username: String, password: String): Boolean {
        val result = client.callMethod(
            "auth.check_password",
            Boolean.serializer(),
            listOf(username, password),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun checkUser(username: String, password: String): Boolean {
        val result = client.callMethod<Boolean, String>(
            "auth.check_user",
            Boolean.serializer(),
            listOf(username, password),
            String.serializer()
        )
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
            String.serializer(),
            listOf(TimeToLive(timeToLive.inWholeSeconds), TokenAttributes(attrs), MatchOrigin(matchOrigin)),
            GenerateTokenParams.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<String> -> result.result
        }
    }

    override suspend fun logIn(username: String, password: String, otpToken: String?): Boolean {
        val result = client.callMethod<Boolean, String?>(
            "auth.login",
            Boolean.serializer(),
            listOf(username, password, otpToken),
            String.serializer().nullable
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logInWithApiKey(apiKey: String): Boolean {
        val result = client.callMethod<Boolean, String>(
            "auth.login_with_api_key",
            Boolean.serializer(),
            listOf(apiKey),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logInWithToken(token: String): Boolean {
        val result = client.callMethod<Boolean, String>(
            "auth.login_with_token",
            Boolean.serializer(),
            listOf(token),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun logOut(): Boolean {
        return when (val result = client.callMethod<Boolean>(
            "auth.logout",
            Boolean.serializer(),
        )) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun me(): AuthenticatedUser {
        return when (val result = client.callMethod<AuthenticatedUser>(
            "auth.me",
            AuthenticatedUser.serializer()
        )) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<AuthenticatedUser> -> result.result
        }
    }

    override suspend fun sessions(): List<Session> {
        return when (val result = client.callMethod<List<Session>>(
            "auth.sessions",
            ListSerializer(Session.serializer())
        )) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<List<Session>> -> result.result
        }
    }

    override suspend fun setAttribute(key: String, value: String) {
        val result = client.callMethod<Unit, String>(
            "auth.set_attribute",
            Unit.serializer(),
            listOf(key, value),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Unit> -> result.result
        }
    }

    override suspend fun terminateOtherSessions() {
        val result = client.callMethod<Unit>(
            "auth.terminate_other_sessions",
            Unit.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Unit> -> result.result
        }
    }

    override suspend fun terminateSession(sessionId: String): Boolean {
        val result = client.callMethod<Boolean, String>(
            "auth.terminate_session",
            Boolean.serializer(),
            listOf(sessionId),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }

    override suspend fun twoFactorAuth(username: String, password: String): Boolean {
        val result = client.callMethod<Boolean, String>(
            "auth.two_factor_auth",
            Boolean.serializer(),
            listOf(username, password),
            String.serializer()
        )
        return when (result) {
            is MethodCallResult.Error<*> -> TODO()
            is MethodCallResult.Success<Boolean> -> result.result
        }
    }
}