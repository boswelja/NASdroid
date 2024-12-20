package com.nasdroid.api.websocket

import com.nasdroid.api.websocket.auth.AuthApi
import com.nasdroid.api.websocket.auth.DdpAuthApi
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.system.DdpSystemApi
import com.nasdroid.api.websocket.system.SystemApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val WebsocketApiModule = module {
    singleOf(::DdpWebsocketClient)
    factoryOf(::DdpAuthApi) bind AuthApi::class
    factoryOf(::DdpSystemApi) bind SystemApi::class
}
