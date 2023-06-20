package com.boswelja.truemanager.core.api.v2.catalog

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

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

    override suspend fun createCatalog(newCatalog: NewCatalog) {
        httpClient.post("catalog") {
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

    override suspend fun syncAll() {
        httpClient.get("catalog/sync_all")
    }

    override suspend fun syncCatalog(id: String) {
        httpClient.post("catalog/sync") {
            setBody(id)
        }
    }

    override suspend fun validateCatalog(id: String): Boolean {
        httpClient.post("catalog/validate") {
            setBody(id)
        }
        return true
    }

}

internal data class CatalogDto(
    val label: String,
    val repository: String,
    val branch: String,
    val builtin: Boolean,
    val preferredTrains: List<String>,
    val location: String,
    val id: String,
)

internal data class NewCatalogDto(
    val label: String,
    val repository: String,
    val branch: String,
    val preferredTrains: List<String>,
    val force: Boolean,
)
