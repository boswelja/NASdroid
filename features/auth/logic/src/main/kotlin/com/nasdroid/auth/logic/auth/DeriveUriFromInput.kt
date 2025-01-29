package com.nasdroid.auth.logic.auth

import com.eygraber.uri.Uri
import com.nasdroid.core.strongresult.StrongResult

class DeriveUriFromInput {

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

        val path = when (uri.path) {
            null -> "websocket"
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

enum class DeriveUriError {
    InvalidScheme,
    InvalidUri
}
