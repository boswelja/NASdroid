package com.nasdroid.api.v2

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

/**
 * Builds a Ktor [HttpClient] for use with the TrueNAS API.
 */
@OptIn(ExperimentalSerializationApi::class)
fun getHttpClient(
    apiStateProvider: ApiStateProvider,
): HttpClient {
    return HttpClient {
        install(io.ktor.client.plugins.logging.Logging) {
            level = io.ktor.client.plugins.logging.LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    Log.i("Ktor", message)
                }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    explicitNulls = false
                }
            )
        }
        defaultRequest {
            when (val authorization = requireNotNull(apiStateProvider.authorization)) {
                is Authorization.ApiKey -> bearerAuth(authorization.apiKey)
                is Authorization.Basic -> basicAuth(authorization.username, authorization.password)
            }
            val baseUrl = requireNotNull(apiStateProvider.serverAddress)
            url(baseUrl)
            contentType(ContentType.Application.Json)
        }
    }
}