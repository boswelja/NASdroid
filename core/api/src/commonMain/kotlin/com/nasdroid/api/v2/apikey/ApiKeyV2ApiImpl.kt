package com.nasdroid.api.v2.apikey

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class ApiKeyV2ApiImpl(
    private val client: HttpClient
) : ApiKeyV2Api {
    override suspend fun getAll(limit: Int?, offset: Int?, sort: String?): List<ApiKey> {
        val response = client.get("api_key") {
            parameter("limit", limit)
            parameter("offset", offset)
            parameter("sort", sort)
        }
        return response.body()
    }

    override suspend fun create(name: String, allowItems: List<AllowRule>): NewApiKey {
        val response = client.post("api_key") {
            setBody(CreateApiKeyBody(name, allowItems))
        }
        return response.body()
    }

    override suspend fun delete(id: Int): Boolean {
        val request = client.delete("api_key/id/$id")
        return request.body()
    }

    override suspend fun get(id: Int): ApiKey {
        val response = client.get("api_key/id/$id")
        return response.body()
    }

    override suspend fun update(id: Int, updatedDetails: UpdateApiKey): ApiKey {
        val response = client.put("api_key/id/$id") {
            setBody(updatedDetails)
        }
        return response.body()
    }

    override suspend fun reset(id: Int): NewApiKey {
        val response = client.put("api_key/id/$id") {
            setBody(mapOf("reset" to true))
        }
        return response.body()
    }
}

@Serializable
internal data class CreateApiKeyBody(
    @SerialName("name")
    val name: String,
    @SerialName("allowlist")
    val allowList: List<AllowRule>
)
