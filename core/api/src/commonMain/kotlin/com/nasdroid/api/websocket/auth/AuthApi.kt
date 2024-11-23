package com.nasdroid.api.websocket.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Describes the TrueNAS API V2 "Auth" group. Note these mappings may not be 1:1, as we will
 * rearrange data to be more accessible in Kotlin.
 */
interface AuthApi {

    /**
     * Verifies the given username/password combination is valid.
     */
    suspend fun checkPassword(username: String, password: String): Boolean

    /**
     * Verifies the given username/password combination is valid.
     */
    suspend fun checkUser(username: String, password: String): Boolean

    /**
     * Generate a token to be used for authentication.
     *
     * @param timeToLive The token will be invalidated if the connection has been inactive for a time
     * greater than this.
     * @param attrs A general purpose object/dictionary to hold information about the token.
     * @param matchOrigin Whether the token will only allow using this token from the same IP
     * address or with the same user UID.
     */
    suspend fun generateToken(
        timeToLive: Duration = 600.seconds,
        attrs: Map<String, Any> = emptyMap(),
        matchOrigin: Boolean = false
    ): String

    /**
     * Authenticate session using username and password. [otpToken] must be specified if two-factor authentication is
     * enabled.
     */
    suspend fun logIn(username: String, password: String, otpToken: String? = null): Boolean

    /**
     * Authenticate session using API key.
     */
    suspend fun logInWithApiKey(apiKey: String): Boolean

    /**
     * Authenticate session using token generated with [generateToken].
     */
    suspend fun logInWithToken(token: String): Boolean

    /**
     * Deauthenticate the session. If a token exists, it is removed from the session.
     */
    suspend fun logOut(): Boolean

    /**
     * Gets the currently logged-in user.
     */
    suspend fun me(): AuthenticatedUser

    /**
     * Get a list of active auth sessions.
     *
     * This method is accessible to the users granted with the following roles:
     * AUTH_SESSIONS_READ, AUTH_SESSIONS_WRITE, READONLY_ADMIN, SHARING_ADMIN
     */
    suspend fun sessions(): List<Session>

    /**
     * Set current users `attributes` dictionary [key] to [value].
     */
    suspend fun setAttribute(key: String, value: Any)

    /**
     * Terminates all other sessions (except the current one).
     *
     * This method is accessible to the users granted with the following roles: AUTH_SESSIONS_WRITE
     */
    suspend fun terminateOtherSessions()

    /**
     * Terminates the session with the given ID.
     */
    suspend fun terminateSession(sessionId: String): Boolean

    /**
     * Returns true if two-factor authorization is required for authorizing user's login.
     */
    suspend fun twoFactorAuth(username: String, password: String): Boolean
}

@Serializable
data class AuthenticatedUser(
    @SerialName("pw_name")
    val name: String,
    @SerialName("pw_gecos")
    val gecos: String,
    @SerialName("pw_dir")
    val dir: String,
    @SerialName("pw_shell")
    val shell: String,
    @SerialName("pw_uid")
    val uid: String,
    @SerialName("pw_gid")
    val gid: String,
    @SerialName("grouplist")
    val groupList: List<String>,
    @SerialName("sid")
    val sid: String,
    @SerialName("source")
    val source: Source,
    @SerialName("local")
    val local: Boolean,
    @SerialName("attributes")
    val attributes: Map<String, @Contextual Any>,
    @SerialName("two_factor_config")
    val twoFactorConfig: Map<String, String> // TODO unknown type
) {
    @Serializable
    enum class Source {
        @SerialName("LOCAL")
        Local,
        @SerialName("ACTIVEDIRECTORY")
        ActiveDirectory,
        @SerialName("LDAP")
        Ldap
    }
}

@Serializable
data class Session(
    @SerialName("id")
    val id: String,
    @SerialName("current")
    val current: Boolean,
    @SerialName("internal")
    val internal: Boolean,
    @SerialName("origin")
    val origin: String,
    @SerialName("credentials")
    val credentials: CredentialType,
    @SerialName("credentials_data")
    val credentialsData: Map<String, String>,
    @SerialName("created_at")
    val createdAt: Instant,
) {
    @Serializable
    enum class CredentialType {
        @SerialName("UNIX_SOCKET")
        UnixSocket,
        @SerialName("ROOT_TCP_SOCKET")
        RootTcpSocket,
        @SerialName("LOGIN_PASSWORD")
        LoginPassword,
        @SerialName("API_KEY")
        ApiKey,
        @SerialName("TOKEN")
        Token
    }
}
