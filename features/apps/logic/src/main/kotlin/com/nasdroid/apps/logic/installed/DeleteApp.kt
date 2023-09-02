package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api

/**
 * Deletes an application and all its data (where possible). See [invoke] for details.
 */
class DeleteApp(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Deletes the specified application, and all its volumes.
     */
    suspend operator fun invoke(releaseName: String, deleteUnusedImages: Boolean) {
        chartReleaseV2Api.deleteRelease(releaseName, deleteUnusedImages)
    }
}
