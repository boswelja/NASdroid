package com.nasdroid.apps.logic.installed

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.api.v2.chart.release.RollbackOptions
import com.nasdroid.apps.data.installed.InstalledAppCache

/**
 * Reverts an app to a previous version. See [invoke] for details.
 */
class RollbackApp(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val installedAppCache: InstalledAppCache,
) {

    /**
     * Rolls back the app to the specified [targetVersion]. If [rollbackSnapshots] is true, then
     * associated storage volumes with snapshots will also be rolled back.
     */
    suspend operator fun invoke(
        appName: String,
        targetVersion: String,
        rollbackSnapshots: Boolean
    ): Result<Unit> {
        return try {
            chartReleaseV2Api.rollbackRelease(
                releaseName = appName,
                options = RollbackOptions(
                    itemVersion = targetVersion,
                    rollbackSnapshot = rollbackSnapshots
                )
            )
            installedAppCache.setAppVersion(
                appName = appName,
                version = targetVersion,
                updateAvailable = true // We assume there will be an update available after downgrading
            )
            Result.success(Unit)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}
