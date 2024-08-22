package com.nasdroid.api.v2.pool

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class PoolV2ApiImpl(
    private val httpClient: HttpClient
) : PoolV2Api {
    override suspend fun getPools(): List<Pool> {
        val response = httpClient.get("pool")
        return response.body()
    }

    override suspend fun createPool(params: CreatePoolParams): Pool {
        val response = httpClient.post("pool") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun updatePool(id: Int, params: UpdatePoolParams): Pool {
        val response = httpClient.post("pool/id/$id") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun getPool(id: Int): Pool {
        val response = httpClient.get("pool/id/$id")
        return response.body()
    }

    override suspend fun validatePoolName(name: String): Boolean {
        val response = httpClient.post("pool/validate_name") {
            contentType(ContentType.Application.Json)
            setBody(name)
        }
        return response.body()
    }
}
