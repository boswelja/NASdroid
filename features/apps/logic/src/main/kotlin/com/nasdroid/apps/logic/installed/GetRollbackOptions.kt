package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api

/**
 * Gets the available rollback configuration for an application installed on the system. See
 * [invoke] for details.
 */
class GetRollbackOptions(
    private val chartReleaseV2Api: ChartReleaseV2Api
){

    /**
     * Gets a [RollbackOptions] for the installed application with the given name.
     */
    suspend operator fun invoke(appName: String): RollbackOptions {
        val result = chartReleaseV2Api.getChartRelease(appName, true)
        val availableVersions = result.history.orEmpty().keys
        return RollbackOptions(
            canRollBack = availableVersions.isNotEmpty(),
            availableVersions = availableVersions
        )
    }
}

/**
 * Describes the configuration available to the user when attempting to roll back an app to an
 * earlier version.
 *
 * @property canRollBack Whether rolling back the app is possible.
 * @property availableVersions A set of available versions that the app can be rolled back to.
 */
data class RollbackOptions(
    val canRollBack: Boolean,
    val availableVersions: Set<String>
)
