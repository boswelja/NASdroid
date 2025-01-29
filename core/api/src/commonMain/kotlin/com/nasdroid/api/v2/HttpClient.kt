package com.nasdroid.api.v2

import com.nasdroid.api.v2.exception.ClientUnauthorizedException
import com.nasdroid.api.v2.exception.HttpNotOkException
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * Builds a Ktor [HttpClient] for use with the TrueNAS API.
 */
fun getRestApiClient(
    apiStateProvider: ApiStateProvider,
): HttpClient {
    return HttpClient {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    println("Ktor: $message")
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

        Charsets {
            register(Charsets.UTF_8)
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
    } catch (_: NoSuchElementException) {
        message ?: response.status.description
    } catch (_: NoTransformationFoundException) {
        message ?: response.status.description
    }
    return when (this) {
        is RedirectResponseException -> {
            com.nasdroid.api.v2.exception.RedirectResponseException(
                code = response.status.value,
                description = message,
                cause = cause
            )
        }
        is ClientRequestException -> {
            if (this.response.status == HttpStatusCode.Unauthorized) {
                ClientUnauthorizedException(description = message, cause = cause)
            } else {
                com.nasdroid.api.v2.exception.ClientRequestException(
                    code = response.status.value,
                    description = message,
                    cause = cause
                )
            }
        }
        is ServerResponseException -> {
            com.nasdroid.api.v2.exception.ServerResponseException(
                code = response.status.value,
                description = message,
                cause = cause
            )
        }
        else -> {
            HttpNotOkException(
                code = response.status.value,
                description = message,
                cause = cause
            )
        }
    }
}
