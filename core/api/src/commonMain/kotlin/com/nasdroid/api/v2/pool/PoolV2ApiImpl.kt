package com.nasdroid.api.v2.pool

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class PoolV2ApiImpl(
    private val httpClient: HttpClient
) : PoolV2Api {
    override suspend fun getPools(): List<Pool> {
        val response = httpClient.get("pool")
        return response.body()
    }

    override suspend fun getPool(id: Int): Pool {
        val response = httpClient.get("pool/id/$id")
        return response.body()
    }
}
