package com.boswelja.truemanager.apps.logic.installed

import android.util.Log
import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api
import com.boswelja.truemanager.core.api.v2.core.CoreV2Api

/**
 * Starts an application that is in a stopped state. See [invoke] for details.
 */
class StartApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val coreV2Api: CoreV2Api,
) {

    /**
     * Starts the application with the given name.
     */
    suspend operator fun invoke(appName: String) {
        val jobId = chartReleaseV2Api.scale(releaseName = appName, replicaCount = 1)
        // Wait for the job to complete
        var isFinished = false
        while (!isFinished) {
            val jobResult = coreV2Api.getJob<String>(jobId)
            Log.d("StartApp", jobResult.state)
            if (jobResult.state == "SUCCESS") {
                isFinished = true
            }
        }
    }
}
