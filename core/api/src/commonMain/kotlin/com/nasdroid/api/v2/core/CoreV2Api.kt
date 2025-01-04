package com.nasdroid.api.v2.core

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.websocket.core.Job
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
     *
     * @throws HttpNotOkException
     */
    suspend fun bulkCallMethod(
        method: String,
        params: List<List<Any>>,
        description: String? = null
    ): Int

    /**
     * Set up `middlewared` for remote debugging.
     *
     * @throws HttpNotOkException
     */
    suspend fun debug(engine: DebugEngine, options: DebugOptions)

    /**
     * Get whether `middlewared` is currently in debug mode.
     *
     * @throws HttpNotOkException
     */
    suspend fun getDebugModeEnabled(): Boolean

    /**
     * Helper to call a job marked for download.
     * Non-buffered downloads will allow job to write to pipe as soon as download URL is requested,
     * job will stay blocked meanwhile. Buffered downloads must wait for job to complete before
     * requesting download URL, job's pipe output will be buffered to ramfs.
     *
     * @throws HttpNotOkException
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
     *
     * @throws HttpNotOkException
     */
    suspend fun getAllJobs(): List<Job<Any>>

    /**
     * Gets information about the job with the given ID, where the result is of type [T].
     *
     * @throws HttpNotOkException
     * @throws JobNotFoundException
     */
    suspend fun <T : Any> getJob(id: Int, type: KClass<T>): Job<T>

    /**
     * Gets information about the job with the given ID, where the result is a list of type [T].
     *
     * @throws HttpNotOkException
     * @throws JobNotFoundException
     */
    suspend fun <T : Any> getJobList(id: Int, type: KClass<T>): Job<List<T>>

    // TODO get_websocket_messages

    /**
     * Aborts the job with the given ID.
     *
     * @throws HttpNotOkException
     */
    suspend fun abortJob(id: Int)

    // TODO job_update
    // TODO job_wait

    /**
     * Ping the system.
     *
     * @throws HttpNotOkException
     */
    suspend fun ping()

    /**
     * Send an ICMP echo request to [hostName] and will wait up to [timeout] seconds for a reply.
     *
     * @throws HttpNotOkException
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
