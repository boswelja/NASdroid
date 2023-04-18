package com.boswelja.truemanager.core.api.v2

import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2ApiImpl
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2ApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiV2Module = module {
    // API state
    singleOf(::InMemoryApiStateProvider) bind ApiStateProvider::class

    // Ktor client
    single {
        val apiStateProvider: ApiStateProvider = get()
        HttpClient(Android) {
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                apiStateProvider.sessionToken?.let { bearerAuth(it) }
                url(apiStateProvider.serverAddress)
            }
        }
    }

    singleOf(::AuthV2ApiImpl) bind AuthV2Api::class
    singleOf(::ReportingV2ApiImpl) bind ReportingV2Api::class
}
