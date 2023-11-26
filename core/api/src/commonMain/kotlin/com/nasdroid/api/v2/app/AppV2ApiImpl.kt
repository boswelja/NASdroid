package com.nasdroid.api.v2.app

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class AppV2ApiImpl(
    private val client: HttpClient
) : AppV2Api {
    override suspend fun getAvailable(): List<AvailableApp> {
        val response = client.get("app/available")
        return response.body()
    }

    override suspend fun getCategories(): List<String> {
        val response = client.get("app/categories")
        return response.body()
    }

    override suspend fun getLatest(): List<AvailableApp> {
        val response = client.get("app/categories")
        return response.body()
    }

    override suspend fun getSimilarTo(
        appName: String,
        catalog: String,
        train: String
    ): List<AvailableApp> {
        val response = client.post("app/similar") {
            contentType(ContentType.Application.Json)
            setBody(SimilarAppRequestBody(appName, catalog, train))
        }
        return response.body()
    }
}

@Serializable
internal data class SimilarAppRequestBody(
    @SerialName("app_name")
    val appName: String,
    @SerialName("catalog")
    val catalog: String,
    @SerialName("train")
    val train: String,
)
