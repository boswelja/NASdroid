package com.nasdroid.storage.logic.pool.import

import com.nasdroid.api.v2.core.CoreV2Api
import com.nasdroid.api.v2.core.Job
import com.nasdroid.api.v2.pool.ImportPoolParams
import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

/**
 * Imports a pool, discovered by [GetImportablePools]. See [invoke] for details.
 */
class ImportPool(
    private val coreV2Api: CoreV2Api,
    private val poolV2Api: PoolV2Api
) {

    /**
     * Imports a pool by its GUID. See [ImportPoolError] for possible failure modes.
     *
     * @param guid The GUID of the pool to import.
     * @param enableAttachments Whether to enable attachments for the imported pool.
     * @param name Optional name for the imported pool. If not specified, the pools original name is
     * used.
     */
    suspend operator fun invoke(
        guid: String,
        enableAttachments: Boolean,
        name: String? = null,
    ): StrongResult<Unit, ImportPoolError> {
        val jobId = poolV2Api.importPool(
            ImportPoolParams(
                guid = guid,
                name = name,
                enableAttachments = enableAttachments
            )
        )
        var job = coreV2Api.getJobList(jobId, Nothing::class)
        withTimeout(Timeout) {
            while (job.state != Job.State.Success) {
                delay(1.seconds)
                job = coreV2Api.getJobList(jobId, Nothing::class)
            }
        }
        return when (job.state) {
            Job.State.Success -> {
                StrongResult.success(Unit)
            }
            Job.State.Failed -> {
                if (job.error == "[ENOENT] Pool  not found.") {
                    StrongResult.failure(ImportPoolError.PoolNotFound)
                } else {
                    StrongResult.failure(ImportPoolError.PoolImportFailed)
                }
            }
        }
    }

    companion object {
        private val Timeout = 30.seconds
    }
}

/**
 * Encapsulates the failure modes for importing a pool.
 */
enum class ImportPoolError {
    PoolNotFound,
    PoolImportFailed
}
