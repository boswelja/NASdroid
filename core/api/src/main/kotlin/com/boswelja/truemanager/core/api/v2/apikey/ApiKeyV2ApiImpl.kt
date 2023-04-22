package com.boswelja.truemanager.core.api.v2.apikey

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant
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
        val dtos: List<ApiKeyDto> = response.body()
        return dtos.map { key ->
            ApiKey(
                id = key.id,
                name = key.name,
                createdAt = Instant.fromEpochMilliseconds(key.createdAt.date),
                allowList = key.allowList.map { ApiKey.AllowRule(it.method, it.resource) }
            )
        }
    }

    override suspend fun create(name: String): String {
        val response = client.post("api_key") {
            contentType(ContentType.Application.Json)
            setBody(CreateApiKeyBody(name, listOf(CreateApiKeyBody.ApiKeyAllowItem("*", "*"))))
        }
        val dto: ApiKeyDto = response.body()
        return requireNotNull(dto.key)
    }

    override suspend fun delete(id: Int) {
        client.delete("api_key/id/$id")
    }

    override suspend fun get(id: Int): ApiKey {
        val response = client.get("api_key/id/$id")
        val dto: ApiKeyDto = response.body()
        return ApiKey(
            id = dto.id,
            name = dto.name,
            createdAt = Instant.fromEpochMilliseconds(dto.createdAt.date),
            allowList = dto.allowList.map { ApiKey.AllowRule(it.method, it.resource) }
        )
    }

    override suspend fun update(id: Int, name: String) {
        client.put("api_key/id/$id") {
            contentType(ContentType.Application.Json)
            setBody(UpdateApiKeyBody(name, null))
        }
    }

    override suspend fun reset(id: Int): String {
        val response = client.put("api_key/id/$id") {
            contentType(ContentType.Application.Json)
            setBody(UpdateApiKeyBody(null, true))
        }
        val dto: ApiKeyDto = response.body()
        return requireNotNull(dto.key)
    }
}

@Serializable
internal data class ApiKeyDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("created_at")
    val createdAt: CreatedAtDto,
    @SerialName("allowlist")
    val allowList: List<AllowRuleDto>,
    @SerialName("key")
    val key: String? = null
) {
    @Serializable
    internal data class AllowRuleDto(
        @SerialName("method")
        val method: String,
        @SerialName("resource")
        val resource: String,
    )

    @Serializable
    internal data class CreatedAtDto(
        @SerialName("\$date")
        val date: Long
    )
}

@Serializable
internal data class CreateApiKeyBody(
    @SerialName("name")
    val name: String,
    @SerialName("allowlist")
    val allowList: List<ApiKeyAllowItem>
) {
    @Serializable
    internal data class ApiKeyAllowItem(
        @SerialName("method")
        val method: String,
        @SerialName("resource")
        val resource: String,
    )
}

@Serializable
internal data class UpdateApiKeyBody(
    @SerialName("name")
    val name: String?,
    @SerialName("reset")
    val reset: Boolean?
)