package com.nasdroid.api.websocket

import com.nasdroid.api.websocket.alert.AlertApi
import com.nasdroid.api.websocket.alert.DdpAlertApi
import com.nasdroid.api.websocket.apiKey.ApiKeyApi
import com.nasdroid.api.websocket.apiKey.DdpApiKeyApi
import com.nasdroid.api.websocket.auth.AuthApi
import com.nasdroid.api.websocket.auth.DdpAuthApi
import com.nasdroid.api.websocket.core.CoreApi
import com.nasdroid.api.websocket.core.DdpCoreApi
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.reporting.DdpReportingApi
import com.nasdroid.api.websocket.reporting.ReportingApi
import com.nasdroid.api.websocket.system.DdpSystemApi
import com.nasdroid.api.websocket.system.SystemApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ApiWebsocketModule = module {
    singleOf(::DdpWebsocketClient)

    singleOf(::DdpAlertApi) bind AlertApi::class
    singleOf(::DdpAuthApi) bind AuthApi::class
    singleOf(::DdpApiKeyApi) bind ApiKeyApi::class
    singleOf(::DdpCoreApi) bind CoreApi::class
    singleOf(::DdpReportingApi) bind ReportingApi::class
    singleOf(::DdpSystemApi) bind SystemApi::class
}
