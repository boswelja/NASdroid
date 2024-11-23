package com.nasdroid.api

import com.nasdroid.api.v2.apikey.ApiKeyV2Api
import com.nasdroid.api.v2.apikey.ApiKeyV2ApiImpl
import com.nasdroid.api.v2.app.AppV2Api
import com.nasdroid.api.v2.app.AppV2ApiImpl
import com.nasdroid.api.v2.auth.AuthV2Api
import com.nasdroid.api.v2.auth.AuthV2ApiImpl
import com.nasdroid.api.v2.catalog.CatalogV2Api
import com.nasdroid.api.v2.catalog.CatalogV2ApiImpl
import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.api.v2.chart.release.ChartReleaseV2ApiImpl
import com.nasdroid.api.v2.core.CoreV2Api
import com.nasdroid.api.v2.core.CoreV2ApiImpl
import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.api.v2.pool.PoolV2ApiImpl
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.ReportingV2ApiImpl
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.api.v2.system.SystemV2ApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module to inject the TrueNAS API V2 dependency graph.
 */
val ApiV2Module = module {
    // API state
    singleOf(::InMemoryApiStateProvider) bind ApiStateProvider::class

    // Ktor client
    singleOf(::getHttpClient)

    singleOf(::ApiKeyV2ApiImpl) bind ApiKeyV2Api::class
    singleOf(::AppV2ApiImpl) bind AppV2Api::class
    singleOf(::AuthV2ApiImpl) bind AuthV2Api::class
    singleOf(::CatalogV2ApiImpl) bind CatalogV2Api::class
    singleOf(::ChartReleaseV2ApiImpl) bind ChartReleaseV2Api::class
    singleOf(::CoreV2ApiImpl) bind CoreV2Api::class
    singleOf(::PoolV2ApiImpl) bind PoolV2Api::class
    singleOf(::ReportingV2ApiImpl) bind ReportingV2Api::class
    singleOf(::SystemV2ApiImpl) bind SystemV2Api::class
}
