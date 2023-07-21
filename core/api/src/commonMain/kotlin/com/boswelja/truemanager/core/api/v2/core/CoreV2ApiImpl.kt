package com.boswelja.truemanager.core.api.v2.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class CoreV2ApiImpl(
    private val client: HttpClient
) : CoreV2Api {
    override suspend fun bulkCallMethod(
        method: String,
        params: List<List<Any>>,
        description: String?
    ): Int {
        val response = client.post("core/bulk") {
            setBody(BulkCallMethodBody(method, params, description))
        }
        return response.body()
    }

    override suspend fun debug(engine: DebugEngine, options: DebugOptions) {
        client.post("core/debug") {
            setBody(DebugBody(engine, options))
        }
    }

    override suspend fun getDebugModeEnabled(): Boolean {
        val response = client.get("core/debug_mode_enabled")
        return response.body()
    }

    override suspend fun getDownloadInfo(
        method: String,
        args: List<Any>,
        fileName: String,
        buffered: Boolean
    ): DownloadInfo {
        val response = client.post("core/download") {
            setBody(DownloadBody(method, args, fileName, buffered))
        }
        return response.body()
    }

    override suspend fun getAllJobs(): List<Job<Any>> {
        val response = client.get("core/get_jobs")
        return response.body()
    }

    override suspend fun <T> getJob(id: Int): Job<T> {
        val response = client.get("core/get_jobs") {
            parameter("id", id)
        }
        val result: String = response.bodyAsText()
        val items: List<Job<T>> = Json.decodeFromString(result)
        return items.first()
    }

    override suspend fun abortJob(id: Int) {
        client.post("core/job_abort") {
            setBody(id)
        }
    }

    override suspend fun ping() {
        client.get("core/ping")
    }

    override suspend fun pingRemote(type: PingType, hostName: String, timeout: Int) {
        client.post("core/ping_remote") {
            setBody(PingRemoteBody(type, hostName, timeout))
        }
    }
}

@Serializable
internal data class BulkCallMethodBody(
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: List<@Contextual Any>,
    @SerialName("description")
    val description: String?,
)

@Serializable
internal data class DebugBody(
    @SerialName("engine")
    val engine: DebugEngine,
    @SerialName("options")
    val options: DebugOptions,
)

@Serializable
internal data class DownloadBody(
    @SerialName("method")
    val method: String,
    @SerialName("args")
    val args: List<@Contextual Any>,
    @SerialName("filename")
    val fileName: String,
    @SerialName("buffered")
    val buffered: Boolean,
)

@Serializable
internal data class PingRemoteBody(
    @SerialName("type")
    val type: PingType,
    @SerialName("hostname")
    val hostname: String,
    @SerialName("timeout")
    val timeout: Int,
)
