package com.nasdroid.api

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun defaultHttpEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = OkHttp
