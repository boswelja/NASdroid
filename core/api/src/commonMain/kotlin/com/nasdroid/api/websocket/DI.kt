package com.nasdroid.api.websocket

import com.nasdroid.api.websocket.auth.DdpAuthApi
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

val WebsocketApiModule = module {
    scope<DdpWebsocketClient> {
        scopedOf(::DdpAuthApi)
    }
}
