package com.boswelja.truemanager.core.api.v2

import com.boswelja.truemanager.core.api.v2.auth.AuthV2Api
import com.boswelja.truemanager.core.api.v2.auth.AuthV2ApiImpl
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2Api
import com.boswelja.truemanager.core.api.v2.reporting.ReportingV2ApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiV2Module = module {
    // Ktor client
    single {
        HttpClient(Android) {
            install(ContentNegotiation)
        }
    }

    singleOf(::AuthV2ApiImpl) bind AuthV2Api::class
    singleOf(::ReportingV2ApiImpl) bind ReportingV2Api::class
}
