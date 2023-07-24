package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

/**
 * Starts an application that is in a stopped state. See [invoke] for details.
 */
class StartApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
) {

    /**
     * Starts the application with the given name.
     */
    suspend operator fun invoke(appName: String) {
        chartReleaseV2Api.scale(releaseName = appName, replicaCount = 1)
        // TODO Wait for the job to complete
    }
}
