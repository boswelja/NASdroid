package com.nasdroid.auth.logic.auth

import com.eygraber.uri.Uri
import com.nasdroid.core.strongresult.StrongResult

/**
 * Takes some user input, and derives a URL pointing to the TrueNAS websocket API. For example, entering `truenas.local`
 * would return `ws://truenas.local/websocket`. Note that no guarantees are made to the validity of the URL. See [invoke]
 * for details.
 */
class DeriveUriFromInput {

    /**
     * Takes an [input] String and attempts to transform it to a URL pointing to a TrueNAS websocket API. For example,
     * entering `truenas.local` would return `ws://truenas.local/websocket`. If this process is not successful, a
     * [DeriveUriError] is returned.
     *
     * Best-effort mapping is performed on the input scheme, where `http/s` is mapped to `ws/s`, and a missing scheme is
     * mapped to ws. It is an error to pass any other scheme.
     *
     * If a path is not specified, `/websocket` is appended automatically.
     */
    operator fun invoke(input: String): StrongResult<String, DeriveUriError> {
        val uri = Uri.parseOrNull(input) ?: return StrongResult.failure(DeriveUriError.InvalidUri)

        val scheme = when (uri.scheme) {
            "ws",
            "http" -> "ws"
            "wss",
            "https" -> "wss"
            null -> "ws"
            else -> return StrongResult.failure(DeriveUriError.InvalidScheme)
        }

        if (uri.authority == null) {
            val authority = uri.pathSegments.firstOrNull() ?: return StrongResult.failure(DeriveUriError.InvalidUri)
            if (authority.contains(" ")) return StrongResult.failure(DeriveUriError.InvalidUri)
            val pathSegments = uri.pathSegments.drop(1).joinToString("/")
            val path = when (pathSegments) {
                "" -> "/websocket"
                else -> pathSegments
            }

            return StrongResult.success(
                uri.buildUpon()
                    .scheme(scheme)
                    .authority(authority)
                    .path(path)
                    .build()
                    .toString()
            )
        } else {
            val path = when (uri.path) {
                null,
                "" -> "/websocket"
                else -> uri.path
            }

            return StrongResult.success(
                uri.buildUpon()
                    .scheme(scheme)
                    .path(path)
                    .build()
                    .toString()
            )
        }
    }
}

/**
 * Encapsulates possible failure modes for [DeriveUriFromInput].
 */
enum class DeriveUriError {
    /**
     * Indicates the input was a correctly formed URI, but the scheme specified was not valid. We expect to use
     * websockets, so a scheme of wither `ws` or `wss` is required.
     */
    InvalidScheme,

    /**
     * Indicates the input provided was not a valid URI.
     */
    InvalidUri
}
