package com.boswelja.truemanager.core.api.v2.catalog

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
        val dtos: List<CatalogDto> = httpClient.get("catalog").body()
        return dtos.map {
            Catalog(
                label = it.label,
                repository = it.repository,
                branch = it.branch,
                builtin = it.builtin,
                preferredTrains = it.preferredTrains,
                location = it.location,
                id = it.id
            )
        }
    }

    override suspend fun createCatalog(newCatalog: NewCatalog): Int {
        val result = httpClient.post("catalog") {
            setBody(
                NewCatalogDto(
                    label = newCatalog.label,
                    repository = newCatalog.repository,
                    branch = newCatalog.branch,
                    preferredTrains = newCatalog.preferredTrains,
                    force = newCatalog.force
                )
            )
        }
        return result.body()
    }

    override suspend fun getCatalog(id: String): Catalog {
        val dto: CatalogDto = httpClient.get("catalog/id/$id").body()
        return Catalog(
            label = dto.label,
            repository = dto.repository,
            branch = dto.branch,
            builtin = dto.builtin,
            preferredTrains = dto.preferredTrains,
            location = dto.location,
            id = dto.id
        )
    }

    override suspend fun deleteCatalog(id: String) {
        httpClient.delete("catalog/id/$id")
    }

    override suspend fun updateCatalog(updatedCatalog: Catalog) {
        httpClient.put("catalog/id/${updatedCatalog.id}") {
            setBody(
                CatalogDto(
                    label = updatedCatalog.label,
                    repository = updatedCatalog.repository,
                    branch = updatedCatalog.branch,
                    builtin = updatedCatalog.builtin,
                    preferredTrains = updatedCatalog.preferredTrains,
                    location = updatedCatalog.location,
                    id = updatedCatalog.id
                )
            )
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
                    options = CatalogItemsBodyDto.CatalogItemsOptionsDto(
                        cache = options.cache,
                        cacheOnly = options.cacheOnly,
                        retrieveAllTrains = options.retrieveAllTrains,
                        trains = options.trains
                    )
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
    val options: CatalogItemsOptionsDto
) {
    @Serializable
    internal data class CatalogItemsOptionsDto(
        @SerialName("cache")
        val cache: Boolean= true,
        @SerialName("cache_only")
        val cacheOnly: Boolean = false,
        @SerialName("retrieve_all_trains")
        val retrieveAllTrains: Boolean = true,
        @SerialName("trains")
        val trains: List<String> = emptyList(),
    )
}

@Serializable
internal data class CatalogDto(
    @SerialName("label")
    val label: String,
    @SerialName("repository")
    val repository: String,
    @SerialName("branch")
    val branch: String,
    @SerialName("builtin")
    val builtin: Boolean,
    @SerialName("preferred_trains")
    val preferredTrains: List<String>,
    @SerialName("location")
    val location: String,
    @SerialName("id")
    val id: String,
)

@Serializable
internal data class NewCatalogDto(
    @SerialName("label")
    val label: String,
    @SerialName("repository")
    val repository: String,
    @SerialName("branch")
    val branch: String,
    @SerialName("preferred_trains")
    val preferredTrains: List<String>,
    @SerialName("force")
    val force: Boolean,
)
