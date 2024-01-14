package com.nasdroid.auth.data

/**
 * Details for a server that has been previously authenticated.
 * @property uid A unique identifier for this server.
 * @property serverAddress The URL of the server. Note this is different from the URL for the API.
 * @property name A friendly name for the server.
 */
data class Server(
    val uid: String,
    val serverAddress: String,
    val name: String,
)