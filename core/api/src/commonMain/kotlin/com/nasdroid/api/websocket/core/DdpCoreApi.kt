package com.nasdroid.api.websocket.core

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.callMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DdpCoreApi(private val client: DdpWebsocketClient) : CoreApi {
    override suspend fun arp(
        ip: String?,
        networkInterface: String?
    ): Map<String, String> {
        return client.callMethod("core.arp", listOf(ArpArgs(ip, networkInterface)))
    }

    override suspend fun debug(
        bindAddress: String,
        bindPort: Int,
        threaded: Boolean
    ) {
        return client.callMethod("core.debug", listOf(DebugArgs(bindAddress, bindPort, threaded)))
    }

    override suspend fun debugModeEnabled(): Boolean {
        return client.callMethod("core.debug_mode_enabled")
    }

    override suspend fun download(
        method: String,
        args: List<String>,
        fileName: String,
        buffered: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getJobs(): List<Job<Any>> {
        return client.callMethod("core.get_jobs")
    }

    override suspend fun jobAbort(jobId: Int) {
        return client.callMethod("core.job_abort", listOf(jobId))
    }

    override suspend fun jobDownloadLogs(
        jobId: Int,
        fileName: String?,
        buffered: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun jobWait(jobId: Int): Int {
        return client.callMethod("core.job_wait", listOf(jobId))
    }

    override suspend fun ping(): String {
        return client.callMethod("core.ping")
    }

    override suspend fun pingRemote(
        hostName: String,
        type: PingType,
        timeout: Int,
        count: Int?,
        networkInterface: String?,
        interval: Int?
    ): Boolean {
        return client.callMethod(method = "core.ping_remote",
            params = listOf(PingRemoteArgs(type, hostName, timeout, count, networkInterface, interval))
        )
    }

    override suspend fun resizeShell(id: String?, cols: Int?, rows: Int?) {
        return client.callMethod("core.resize_shell", listOf(id, cols, rows))
    }

    override suspend fun sessions(): List<Session> {
        return client.callMethod("core.sessions")
    }

    override suspend fun setDebugMode(debugMode: Boolean) {
        return client.callMethod("core.set_debug_mode", listOf(debugMode))
    }
}

@Serializable
internal data class ArpArgs(
    @SerialName("ip")
    val ip: String?,
    @SerialName("interface")
    val networkInterface: String?
)

@Serializable
internal data class DebugArgs(
    @SerialName("bind_address")
    val bindAddress: String,
    @SerialName("bind_port")
    val bindPort: Int,
    @SerialName("threaded")
    val threaded: Boolean,
)

@Serializable
internal data class PingRemoteArgs(
    @SerialName("type")
    val type: PingType?,
    @SerialName("hostname")
    val hostName: String,
    @SerialName("timeout")
    val timeout: Int?,
    @SerialName("count")
    val count: Int?,
    @SerialName("interface")
    val networkInterface: String?,
    @SerialName("interval")
    val interval: Int?
)