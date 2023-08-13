package com.boswelja.truemanager.core.api.v2.core

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject

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
    @Serializable(with = UnwrappingDateSerializer::class)
    @SerialName("time_started")
    val timeStarted: Long,
    @Serializable(with = UnwrappingDateSerializer::class)
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


internal class UnwrappingDateSerializer : JsonTransformingSerializer<Long>(Long.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return if (element is JsonObject) {
            element.getValue("\$date")
        } else {
            super.transformDeserialize(element)
        }
    }

    override fun transformSerialize(element: JsonElement): JsonElement {
        return buildJsonObject {
            put("\$date", element)
        }
    }
}
