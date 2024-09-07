package com.nasdroid.storage.logic.pool.import

import com.nasdroid.api.v2.core.CoreV2Api
import com.nasdroid.api.v2.core.Job
import com.nasdroid.api.v2.pool.ImportablePool
import com.nasdroid.api.v2.pool.PoolV2Api
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.seconds

/**
 * Retrieves a list of pools that can be imported. See [invoke] for details.
 */
class GetImportablePools(
    private val coreV2Api: CoreV2Api,
    private val poolV2Api: PoolV2Api
) {

    /**
     * Retrieves a list of pools that can be imported, or [GetImportablePoolsError] if an error
     * occurs. Note this is a potentially long-running action.
     */
    suspend operator fun invoke(): StrongResult<List<ImportablePool>, GetImportablePoolsError> {
        val jobId = poolV2Api.findPoolsToImport()
        var job = coreV2Api.getJobList(jobId, ImportablePool::class)
        withTimeout(Timeout) {
            while (job.state != Job.State.Success) {
                delay(1.seconds)
                job = coreV2Api.getJobList(jobId, ImportablePool::class)
            }
        }
        return if (job.state != Job.State.Success) {
            StrongResult.failure(GetImportablePoolsError.Timeout)
        } else {
            StrongResult.success(job.result!!)
        }
    }

    companion object {
        private val Timeout = 30.seconds
    }
}

/**
 * Encapsulates all possible failures when retrieving importable pools.
 */
enum class GetImportablePoolsError {
    Timeout
}
