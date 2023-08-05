package com.boswelja.truemanager.auth.logic.manageservers

/**
 * A Server that has been stored in the apps authenticated server store.
 *
 * @property name The user-defined name of the server.
 * @property url The URL at which the server is reachable.
 * @property id The server ID.
 */
data class Server(
    val name: String,
    val url: String,
    val id: String
)
