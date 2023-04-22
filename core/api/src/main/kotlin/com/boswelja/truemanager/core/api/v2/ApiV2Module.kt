package com.boswelja.truemanager.core.api.v2

import android.util.Log
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2Api
import com.boswelja.truemanager.core.api.v2.apikey.ApiKeyV2ApiImpl
import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2ApiImpl
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2ApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the auth dependency graph. This depends on the API module.
 */
@OptIn(ExperimentalSerializationApi::class)
val apiV2Module = module {
    // API state
    singleOf(::InMemoryApiStateProvider) bind ApiStateProvider::class

    // Ktor client
    single {
        val apiStateProvider: ApiStateProvider = get()
        HttpClient(Android) {
            // TODO if debug, BuildConfig appears to be missing
            install(io.ktor.client.plugins.logging.Logging) {
                level = io.ktor.client.plugins.logging.LogLevel.ALL
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Log.i("Ktor", message)
                    }
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                })
            }
            defaultRequest {
                when (val authorization = requireNotNull(apiStateProvider.authorization)) {
                    is Authorization.ApiKey -> bearerAuth(authorization.apiKey)
                    is Authorization.Basic -> basicAuth(authorization.username, authorization.password)
                }
                val baseUrl = requireNotNull(apiStateProvider.serverAddress)
                url(baseUrl)
            }
        }
    }

    singleOf(::ApiKeyV2ApiImpl) bind ApiKeyV2Api::class
    singleOf(::AuthV2ApiImpl) bind AuthV2Api::class
    singleOf(::ReportingV2ApiImpl) bind ReportingV2Api::class
}
