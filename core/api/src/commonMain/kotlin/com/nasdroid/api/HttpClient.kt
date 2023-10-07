package com.nasdroid.api

import android.util.Log
import com.nasdroid.api.exception.HttpNotOkException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

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

        // We want Ktor to give us nice non-2xx exceptions to make mapping easier
        expectSuccess = true
        HttpResponseValidator {
            // Handle non-2xx response codes
            handleResponseExceptionWithRequest { cause, _ ->
                throw when (cause) {
                    is ResponseException -> cause.toHttpNotOkException()
                    else -> cause
                }
            }
        }
    }
}

private suspend fun ResponseException.toHttpNotOkException(): HttpNotOkException {
    // Try to extract the error message from TrueNAS
    val message = try {
        response.body<JsonObject>().getValue("message").toString()
    } catch (_: JsonConvertException) {
        message ?: response.status.description
    }
    return when (this) {
        is RedirectResponseException -> com.nasdroid.api.exception.RedirectResponseException(
            code = response.status.value,
            description = message,
            cause = cause
        )
        is ClientRequestException -> com.nasdroid.api.exception.ClientRequestException(
            code = response.status.value,
            description = message,
            cause = cause
        )
        is ServerResponseException -> com.nasdroid.api.exception.ServerResponseException(
            code = response.status.value,
            description = message,
            cause = cause
        )
        else -> HttpNotOkException(
            code = response.status.value,
            description = message,
            cause = cause
        )
    }
}
