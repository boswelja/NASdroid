package com.nasdroid.api.v2.catalog

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class CatalogV2ApiImpl(
    private val httpClient: HttpClient
) : CatalogV2Api {
    override suspend fun getCatalogs(): List<Catalog> {
        return httpClient.get("catalog").body()
    }

    override suspend fun createCatalog(newCatalog: NewCatalog): Int {
        val result = httpClient.post("catalog") {
            setBody(newCatalog)
        }
        return result.body()
    }

    override suspend fun getCatalog(id: String): Catalog {
        return httpClient.get("catalog/id/$id").body()
    }

    override suspend fun deleteCatalog(id: String) {
        httpClient.delete("catalog/id/$id")
    }

    override suspend fun updateCatalog(updatedCatalog: Catalog) {
        httpClient.put("catalog/id/${updatedCatalog.id}") {
            setBody(updatedCatalog)
        }
    }

    override suspend fun syncAll(): Int {
        val result = httpClient.get("catalog/sync_all")
        return result.body()
    }

    override suspend fun syncCatalog(id: String): Int {
        val result = httpClient.post("catalog/sync") {
            setBody(id)
        }
        return result.body()
    }

    override suspend fun validateCatalog(id: String): Int {
        val result = httpClient.post("catalog/validate") {
            setBody(id)
        }
        return result.body()
    }

    override suspend fun getCatalogItems(id: String, options: GetCatalogItemsOptions): Int {
        val request = httpClient.post("catalog/items") {
            setBody(
                CatalogItemsBodyDto(
                    label = id,
                    options = options
                )
            )
        }
        return request.body()
    }
}

@Serializable
internal data class CatalogItemsBodyDto(
    @SerialName("label")
    val label: String,
    @SerialName("options")
    val options: GetCatalogItemsOptions
)
