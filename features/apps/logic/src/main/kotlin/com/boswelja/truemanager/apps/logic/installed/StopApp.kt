package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

/**
 * Stops an application that is in a started state. See [invoke] for details.
 */
class StopApp(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Stops the application with the given name.
     */
    suspend operator fun invoke(appName: String) {
        chartReleaseV2Api.scale(releaseName = appName, replicaCount = 0)
    }
}
