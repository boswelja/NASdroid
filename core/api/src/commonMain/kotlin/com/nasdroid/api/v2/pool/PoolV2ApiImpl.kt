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

    override suspend fun attachDiskToPool(params: AttachPoolParams) {
        val response = httpClient.post("pool/attach") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun getPoolAttachments(id: Int): List<PoolAttachment> {
        val response = httpClient.post("pool/id/$id/attachments")
        return response.body()
    }

    override suspend fun detachDiskFromPool(id: Int, label: String): Boolean {
        val response = httpClient.post("pool/id/$id/detach") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("label" to label))
        }
        return response.body()
    }

    override suspend fun expandPool(id: Int) {
        val response = httpClient.post("pool/id/$id/expand")
        return response.body()
    }

    override suspend fun exportPool(id: Int, params: ExportPoolParams) {
        val response = httpClient.post("pool/id/$id/export") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun getFilesystemChoices(types: List<FileSystemType>): List<String> {
        val response = httpClient.post("pool/filesystem_choices") {
            contentType(ContentType.Application.Json)
            setBody(types)
        }
        return response.body()
    }

    override suspend fun getPoolDisks(id: Int): List<String> {
        val response = httpClient.post("pool/id/$id/get_disks")
        return response.body()
    }

    override suspend fun findPoolsToImport(): Int {
        val response = httpClient.get("pool/import_find")
        return response.body()
    }

    override suspend fun importPool(params: ImportPoolParams): Int {
        val response = httpClient.post("pool/import_pool") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun isPoolUpgraded(id: Int): Boolean {
        val response = httpClient.post("pool/id/$id/is_upgraded")
        return response.body()
    }

    override suspend fun offlineDiskFromPool(id: Int, label: String): Boolean {
        val response = httpClient.post("pool/id/$id/offline") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("label" to label))
        }
        return response.body()
    }

    override suspend fun onlineDiskFromPool(id: Int, label: String): Boolean {
        val response = httpClient.post("pool/id/$id/online") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("label" to label))
        }
        return response.body()
    }

    override suspend fun getPoolProcesses(id: Int): List<String> {
        val response = httpClient.post("pool/id/$id/processes")
        return response.body()
    }

    override suspend fun removeDiskFromPool(id: Int, label: String) {
        val response = httpClient.post("pool/id/$id/remove") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("label" to label))
        }
        return response.body()
    }

    override suspend fun replaceDiskInPool(id: Int, params: ReplaceDiskParams): Boolean {
        val response = httpClient.post("pool/id/$id/replace") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
        return response.body()
    }

    override suspend fun scrubPool(id: Int, action: ScrubAction) {
        val response = httpClient.post("pool/id/$id/scrub") {
            contentType(ContentType.Application.Json)
            setBody(action)
        }
        return response.body()
    }

    override suspend fun upgradePool(id: Int): Boolean {
        val response = httpClient.post("pool/id/$id/upgrade")
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
