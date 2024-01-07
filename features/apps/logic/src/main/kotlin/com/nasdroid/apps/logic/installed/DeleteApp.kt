package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.apps.data.installed.InstalledAppCache

/**
 * Deletes an application and all its data (where possible). See [invoke] for details.
 */
class DeleteApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val installedAppCache: InstalledAppCache,
) {

    /**
     * Deletes the specified application, and all its volumes.
     */
    suspend operator fun invoke(appName: String, deleteUnusedImages: Boolean) {
        installedAppCache.deleteInstalledApp(appName)
        chartReleaseV2Api.deleteRelease(appName, deleteUnusedImages)
    }
}
