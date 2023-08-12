package com.boswelja.truemanager.core.api.v2.core

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlin.reflect.KClass

/**
 * Describes the TrueNAS API V2 "Core" group.
 */
interface CoreV2Api {

    /**
     * Sequentially calls [method] with each set of parameters from [params]. For example, calling
     * ```kt
     * bulkCallMethod(
     *   "zfs.snapshot.delete",
     *   listOf(
     *     listOf("tank@snap-1", true),
     *     listOf("tank@snap-2", false)
     *   )
     * )
     * ```
     * Will execute
     * ```
     * call("zfs.snapshot.delete", "tank@snap-1", true)
     * call("zfs.snapshot.delete", "tank@snap-2", false)
     * ```
     * on the system.
     *
     * @return A job ID to monitor status with. TODO Job system
     */
    suspend fun bulkCallMethod(
        method: String,
        params: List<List<Any>>,
        description: String? = null
    ): Int

    /**
     * Set up `middlewared` for remote debugging.
     */
    suspend fun debug(engine: DebugEngine, options: DebugOptions)

    /**
     * Get whether `middlewared` is currently in debug mode.
     */
    suspend fun getDebugModeEnabled(): Boolean

    /**
     * Helper to call a job marked for download.
     * Non-buffered downloads will allow job to write to pipe as soon as download URL is requested,
     * job will stay blocked meanwhile. Buffered downloads must wait for job to complete before
     * requesting download URL, job's pipe output will be buffered to ramfs.
     */
    suspend fun getDownloadInfo(
        method: String,
        args: List<Any>,
        fileName: String,
        buffered: Boolean
    ): DownloadInfo

    // TODO get_events

    /**
     * Get all long-running jobs.
     */
    suspend fun getAllJobs(): List<Job<Any>>

    /**
     * Gets information about the job with the given ID.
     */
    suspend fun <T : Any> getJob(id: Int, type: KClass<T>): Job<T>

    // TODO get_websocket_messages

    /**
     * Aborts the job with the given ID.
     */
    suspend fun abortJob(id: Int)

    // TODO job_update
    // TODO job_wait

    /**
     * Ping the system.
     */
    suspend fun ping()

    /**
     * Send an ICMP echo request to [hostName] and will wait up to [timeout] seconds for a reply.
     */
    suspend fun pingRemote(type: PingType, hostName: String, timeout: Int)
}

suspend inline fun <reified T: Any> CoreV2Api.getJob(id: Int): Job<T> {
    return getJob(id, T::class)
}

/**
 * Available debug engines for middlewared.
 */
@Serializable
enum class DebugEngine {
    @SerialName("PTVS")
    PTVS,
    @SerialName("PYDEV")
    PYDEV,
    @SerialName("REMOTE_PDB")
    REMOTE_PDB
}

/**
 * Options for middlewared debugging.
 *
 * @property secret Password for PTVS.
 * @property host Required for PYDEV, hostname of local computer (developer workstation).
 * @property localPath Required for PYDEV, path for middlewared source in local computer
 * (e.g. /home/user/freenas/src/middlewared/middlewared).
 * @property bindAddress TODO
 * @property bindPort TODO
 * @property waitAttach TODO
 * @property threaded Run debugger in a new thread instead of event loop.
 */
@Serializable
data class DebugOptions(
    @SerialName("secret")
    val secret: String,
    @SerialName("host")
    val host: String,
    @SerialName("local_path")
    val localPath: String,
    @SerialName("bind_address")
    val bindAddress: String = "0.0.0.0",
    @SerialName("bind_port")
    val bindPort: Int = 3000,
    @SerialName("wait_attach")
    val waitAttach: Boolean = false,
    @SerialName("threaded")
    val threaded: Boolean = false,
)

/**
 * Contains information about a downloadable file from a job.
 *
 * @property id The ID of the job.
 * @property url The URL used to download the file.
 */
@Serializable
data class DownloadInfo(
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
)

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
 * @property error A human-readable error message, if any.
 * @property exception The exception that occurred when the job failed.
 * @property excInfo TODO
 * @property state The state of the job.
 * @property timeStarted The timestamp of when this job was started.
 * @property timeFinished The timestamp of when this job finished, if any.
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
    val progress: JobProgress,
    @SerialName("result")
    val result: T?,
    @SerialName("error")
    val error: String?,
    @SerialName("exception")
    val exception: String?,
    @SerialName("exc_info")
    val excInfo: JobExcInfo?,
    @SerialName("state")
    val state: String,
    @SerialName("time_started")
    val timeStarted: Long,
    @SerialName("time_finished")
    val timeFinished: Long?
)

/**
 * Describes the current progress of a running job.
 *
 * @property percent The percentage of work completed. Between 0 and 100.
 * @property description A description for the job progress.
 * @property extra TODO
 */
@Serializable
data class JobProgress(
    @SerialName("percent")
    val percent: Int,
    @SerialName("description")
    val description: String,
    @Contextual
    @SerialName("extra")
    val extra: Any?
)

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

/**
 * Different ping methods available for pinging remote hosts. See [CoreV2Api.pingRemote].
 */
@Serializable
enum class PingType {
    @SerialName("ICMP")
    ICMP,
    @SerialName("ICMPV4")
    ICMPV4,
    @SerialName("ICMPV6")
    ICMPV6
}
