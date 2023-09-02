package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.api.v2.chart.release.RollbackOptions

/**
 * Reverts an app to a previous version. See [invoke] for details.
 */
class RollbackApp(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Rolls back the app to the specified [targetVersion]. If [rollbackSnapshots] is true, then
     * associated storage volumes with snapshots will also be rolled back.
     */
    suspend operator fun invoke(
        releaseName: String,
        targetVersion: String,
        rollbackSnapshots: Boolean
    ) {
        chartReleaseV2Api.rollbackRelease(
            releaseName = releaseName,
            options = RollbackOptions(
                itemVersion = targetVersion,
                rollbackSnapshot = rollbackSnapshots
            )
        )
    }
}
