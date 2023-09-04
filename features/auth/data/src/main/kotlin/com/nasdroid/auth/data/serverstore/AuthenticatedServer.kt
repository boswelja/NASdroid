package com.nasdroid.auth.data.serverstore

/**
 * Details for a server that has been previously authenticated.
 */
sealed interface AuthenticatedServer {

    /**
     * A unique identifier for this server.
     */
    val uid: String

    /**
     * The URL of the server. Note this is different from the URL for the API.
     */
    val serverAddress: String

    /**
     * A friendly name for the server.
     */
    val name: String

    /**
     * A server that is authenticated via username and password.
     *
     * @property uid A unique identifier for this server.
     * @property serverAddress The URL of the server. Note this is different from the URL for the API.
     * @property name A friendly name for the server.
     * @property username The username to use for authentication.
     * @property password The password to use for authentication.
     */
    data class Basic(
        override val uid: String,
        override val serverAddress: String,
        override val name: String,
        val username: String,
        val password: String,
    ): AuthenticatedServer

    /**
     * A server that is authenticated via an API key.
     *
     * @property uid A unique identifier for this server.
     * @property serverAddress The URL of the server. Note this is different from the URL for the API.
     * @property name A friendly name for the server.
     * @property apiKey The API key to use for authentication.
     */
    data class ApiKey(
        override val uid: String,
        override val serverAddress: String,
        override val name: String,
        val apiKey: String,
    ): AuthenticatedServer
}
