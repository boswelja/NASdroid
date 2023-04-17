package com.boswelja.truemanager.core.api.v2.auth

import kotlin.time.Duration

interface AuthV2Api {

    /**
     * Verifies the given username/password combination is valid.
     */
    suspend fun checkPassword(username: String, password: String): Boolean

    /**
     * Verifies the given username/password combination is valid.
     */
    suspend fun checkUser(username: String, password: String): Boolean

    /**
     * Generates a new session token.
     */
    suspend fun generateToken(
        username: String,
        password: String,
        timeToLive: Duration,
        attrs: Map<String, Any>,
        matchOrigin: Boolean
    ): String

    /**
     * Terminates the session with the given ID.
     */
    suspend fun terminateSession(sessionId: String)

    /**
     * Checks whether two-factor-authentication is required to connect to the server.
     */
    suspend fun twoFactorAuth(): Boolean
}
