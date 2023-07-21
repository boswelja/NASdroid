package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api
import com.boswelja.truemanager.core.api.v2.core.CoreV2Api

/**
 * Stops an application that is in a started state. See [invoke] for details.
 */
class StopApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val coreV2Api: CoreV2Api
) {

    /**
     * Stops the application with the given name.
     */
    suspend operator fun invoke(appName: String) {
        val jobId = chartReleaseV2Api.scale(releaseName = appName, replicaCount = 0)
        // Wait for the job to complete
        var isFinished = false
        while (!isFinished) {
            val jobResult = coreV2Api.getJob<String>(jobId)
            if (jobResult.state != "RUNNING") {
                isFinished = true
            }
        }
    }
}
