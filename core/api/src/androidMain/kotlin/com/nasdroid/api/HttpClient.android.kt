package com.nasdroid.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun defaultHttpEngine(): HttpClientEngine = OkHttp.create { }
