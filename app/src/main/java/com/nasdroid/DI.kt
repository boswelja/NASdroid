package com.nasdroid

import com.nasdroid.api.v2.ApiV2Module
import com.nasdroid.api.websocket.ApiWebsocketModule
import com.nasdroid.apps.ui.AppsModule
import com.nasdroid.auth.ui.AuthModule
import com.nasdroid.dashboard.ui.DashboardModule
import com.nasdroid.power.ui.PowerModule
import com.nasdroid.reporting.ui.ReportingModule
import com.nasdroid.storage.ui.StorageUiModule
import org.koin.dsl.module

/**
 * A Koin module that contains the entire dependency graph.
 */
val NasDroidModule = module {
    includes(ApiV2Module)
    includes(ApiWebsocketModule)

    includes(
        AppsModule,
        AuthModule,
        DashboardModule,
        PowerModule,
        ReportingModule,
        StorageUiModule,
    )
}
