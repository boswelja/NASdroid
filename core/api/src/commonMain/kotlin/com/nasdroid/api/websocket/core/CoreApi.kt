package com.nasdroid.api.websocket.core

import com.nasdroid.api.v2.core.CoreV2Api
import com.nasdroid.api.websocket.ddp.EDateInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

interface CoreApi {

    /**
     * https://en.wikipedia.org/wiki/Address_Resolution_Protocol.
     */
    suspend fun arp(ip: String? = null, networkInterface: String? = null): Map<String, String>

    /**
     * Will sequentially call method with arguments from the params list. For example, running
     *
     * ```
     * call("core.bulk", "zfs.snapshot.delete", [["tank@snap-1", true], ["tank@snap-2", false]])
     * ```
     *
     * will call
     *
     * ```
     * call("zfs.snapshot.delete", "tank@snap-1", true)
     * call("zfs.snapshot.delete", "tank@snap-2", false)
     * ```
     *
     * If the first call fails and the seconds succeeds (returning true), the result of the overall call will be:
     *
     * ```
     * [
     *     {"result": null, "error": "Error deleting snapshot"},
     *     {"result": true, "error": null}
     * ]
     * ```
     *
     * Important note: the execution status of core.bulk will always be a SUCCESS (unless an
     * unlikely internal error occurs). Caller must check for individual call results to ensure the
     * absence of any call errors.
     */
    suspend fun bulk(method: String, params: List<List<String>>): Int

    /**
     * Setup middlewared for remote debugging.
     *
     * engine currently used: - REMOTE_PDB: Remote vanilla PDB (over TCP sockets)
     *
     * @param bindAddress: local ip address to bind the remote debug session to
     * @param bindPort: local port to listen on
     * @param threaded: run debugger in a new thread instead of the main event loop
     */
    suspend fun debug(bindAddress: String = "0.0.0.0", bindPort: Int = 3000, threaded: Boolean = false)

    /**
     * -
     */
    suspend fun debugModeEnabled(): Boolean

    /**
     * Core helper to call a job marked for download.
     *
     * Non-buffered downloads will allow job to write to pipe as soon as download URL is requested,
     * job will stay blocked meanwhile. buffered downloads must wait for job to complete before
     * requesting download URL, job's pipe output will be buffered to ramfs.
     *
     * Returns the job id and the URL for download.
     */
    suspend fun download(method: String, args: List<String>, fileName: String, buffered: Boolean = false)

    /**
     * Returns metadata for every possible event emitted from websocket server.
     */
    suspend fun getEvents(): List<String>

    /**
     * Get information about long-running jobs. If authenticated session does not have the
     * FULL_ADMIN role, only jobs owned by the current authenticated session will be returned.
     */
    suspend fun getJobs(): List<Job<Any>>

    /**
     * Aborts the job with the given ID.
     */
    suspend fun jobAbort(jobId: Int)

    /**
     * Download logs of the job id.
     *
     * Please see [download] method documentation for explanation on filename and buffered
     * arguments, and return value.
     */
    suspend fun jobDownloadLogs(jobId: Int, fileName: String?, buffered: Boolean = false)

    /**
     * Starts a new Job that waits for the specified job to complete.
     */
    suspend fun jobWait(jobId: Int): Int

    /**
     * Utility method which just returns "pong". Can be used to keep connection/authtoken alive
     * instead of using "ping" protocol message.
     */
    suspend fun ping(): String

    /**
     * Method that will send an ICMP echo request to "hostname" and will wait up to "timeout" for a
     * reply.
     */
    suspend fun pingRemote(
        hostName: String,
        type: PingType = PingType.ICMP,
        timeout: Int = 4,
        count: Int? = null,
        networkInterface: String? = null,
        interval: Int? = null
    ): Boolean

    /**
     * Resize terminal session (/websocket/shell) to cols x rows.
     */
    suspend fun resizeShell(id: String?, cols: Int?, rows: Int?)

    /**
     * Get currently open websocket sessions.
     */
    suspend fun sessions(): List<Session>

    /**
     * Set debug_mode for middleware.
     */
    suspend fun setDebugMode(debugMode: Boolean)
}

@Serializable
data class Session(
    @SerialName("id")
    val id: String,
    @SerialName("socket_family")
    val socketFamily: String,
    @SerialName("address")
    val address: String,
    @SerialName("authenticated")
    val authenticated: Boolean,
    @SerialName("call_count")
    val callCount: Int
)

@Serializable
enum class PingType {
    @SerialName("ICMP")
    ICMP,
    @SerialName("ICMPV4")
    ICMPV4,
    @SerialName("ICMPV6")
    ICMPV6
}

/**
 * Contains information about a single job on the system.
 *
 * @param T The type of result expected from the job.
 * @property id The job ID.
 * @property method The method this job called.
 * @property arguments A list of arguments the method was called with.
 * @property transient TODO
 * @property description A human-readable description of the job.
 * @property abortable Whether the job can be aborted. See [CoreV2Api.abortJob].
 * @property logsPath TODO
 * @property logsExcerpt TODO
 * @property progress Information about the jobs current progress.
 * @property result The result of the job, or null if there is no result.
 * @property resultEncodingError TODO
 * @property error A human-readable error message, if any.
 * @property exception The exception that occurred when the job failed.
 * @property excInfo TODO
 * @property state The state of the job.
 * @property timeStarted The timestamp of when this job was started.
 * @property timeFinished The timestamp of when this job finished, if any.
 * @property credentials The credentials of the user that initiated the job.
 */
@Serializable
data class Job<T>(
    @SerialName("id")
    val id: Int,
    @SerialName("method")
    val method: String,
    @SerialName("arguments")
    val arguments: List<JsonElement>, // TODO Serializer
    @SerialName("transient")
    val transient: Boolean,
    @SerialName("description")
    val description: String?,
    @SerialName("abortable")
    val abortable: Boolean,
    @SerialName("logs_path")
    val logsPath: String?,
    @SerialName("logs_excerpt")
    val logsExcerpt: String?,
    @SerialName("progress")
    val progress: Progress,
    @SerialName("result")
    val result: T?,
    @SerialName("result_encoding_error")
    val resultEncodingError: String?,
    @SerialName("error")
    val error: String?,
    @SerialName("exception")
    val exception: String?,
    @SerialName("exc_info")
    val excInfo: JobExcInfo?,
    @SerialName("state")
    val state: State,
    @Serializable(with = EDateInstantSerializer::class)
    @SerialName("time_started")
    val timeStarted: Instant,
    @Serializable(with = EDateInstantSerializer::class)
    @SerialName("time_finished")
    val timeFinished: Instant?,
    @SerialName("credentials")
    val credentials: Credentials?
) {

    @Serializable
    data class Credentials(
        @SerialName("type")
        val type: String,
        @SerialName("data")
        val data: Map<String, String>
    )
    /**
     * Describes the current progress of a running job.
     *
     * @property percent The percentage of work completed. Between 0 and 100.
     * @property description A description for the job progress.
     * @property extra TODO
     */
    @Serializable
    data class Progress(
        @SerialName("percent")
        val percent: Int,
        @SerialName("description")
        val description: String,
        @Contextual
        @SerialName("extra")
        val extra: Any?
    )

    /**
     * Encapsulates all possible states for a [Job].
     */
    @Serializable
    enum class State {
        @SerialName("SUCCESS")
        Success,
        @SerialName("FAILED")
        Failed,
        @SerialName("starting")
        Starting,
        @SerialName("running")
        Running,
        @SerialName("exited")
        Exited,
        @SerialName("DEPLOYING")
        Deploying,
    }
}

/**
 * TODO What is this?
 *
 * @property repr TODO
 * @property type TODO
 * @property extra TODO
 */
@Serializable
data class JobExcInfo(
    @SerialName("repr")
    val repr: String?,
    @SerialName("type")
    val type: String?,
    @Contextual
    @SerialName("extra")
    val extra: Any?
)
