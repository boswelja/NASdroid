package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.apps.data.installed.CachedInstalledApp
import com.nasdroid.apps.data.installed.InstalledAppCache

/**
 * Stops an application that is in a started state. See [invoke] for details.
 */
class StopApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val installedAppCache: InstalledAppCache,
) {

    /**
     * Stops the application with the given name.
     */
    suspend operator fun invoke(appName: String) {
        installedAppCache.setState(appName, CachedInstalledApp.State.STOPPED)
        chartReleaseV2Api.scale(releaseName = appName, replicaCount = 0)
        // TODO Wait for the job to complete
    }
}
