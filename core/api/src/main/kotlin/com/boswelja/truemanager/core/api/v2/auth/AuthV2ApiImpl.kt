package com.boswelja.truemanager.core.api.v2.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

internal class AuthV2ApiImpl(
    private val client: HttpClient
) : AuthV2Api {
    override suspend fun checkPassword(username: String, password: String): Boolean {
        val response = client.post("/auth/check_password") {
            setBody(UserCredentialsDto(username, password))
            basicAuth(username, password)
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun checkUser(username: String, password: String): Boolean {
        val response = client.post("/auth/check_user") {
            setBody(UserCredentialsDto(username, password))
            basicAuth(username, password)
        }
        return response.status == HttpStatusCode.OK
    }

    override suspend fun generateToken(
        username: String,
        password: String,
        timeToLive: Duration,
        attrs: Map<String, Any>,
        matchOrigin: Boolean
    ): String {
        val response = client.post("/auth/generate_token") {
            setBody(SessionTokenRequestDto(timeToLive.inWholeSeconds, attrs, matchOrigin))
            basicAuth(username, password)
        }
        return response.body()
    }

    override suspend fun terminateSession(sessionId: String) {
        val response = client.post("/auth/generate_token") {
            setBody(sessionId)
        }
        return response.body()
    }

    override suspend fun twoFactorAuth(): Boolean {
        val response = client.get("/auth/two_factor_auth")
        return response.body()
    }
}

@Serializable
internal data class UserCredentialsDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
)

@Serializable
internal data class SessionTokenRequestDto(
    @SerialName("ttl")
    val timeToLive: Long,
    @SerialName("attrs")
    val attrs: Map<String, @Contextual Any>,
    @SerialName("match_origin")
    val matchOrigin: Boolean,
)