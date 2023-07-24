package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

/**
 * Gets a list of applications installed on the system. See [invoke] for details.
 */
class GetInstalledApps(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Gets a list of all applications installed on the system.
     */
    suspend operator fun invoke(): List<ApplicationOverview> {
        val releaseDtos = chartReleaseV2Api.getChartReleases()
        return releaseDtos.map { chartRelease ->
            ApplicationOverview(
                name = chartRelease.id,
                version = chartRelease.humanVersion,
                iconUrl = chartRelease.chartMetadata.icon,
                catalog = chartRelease.catalog,
                train = chartRelease.catalogTrain,
                state = when (chartRelease.status) {
                    "ACTIVE" -> ApplicationOverview.State.ACTIVE
                    "STOPPED" -> ApplicationOverview.State.STOPPED
                    "DEPLOYING" -> ApplicationOverview.State.DEPLOYING
                    else -> error("Unhandled state: ${chartRelease.status}")
                },
                updateAvailable = chartRelease.updateAvailable,
                webPortalUrl = chartRelease.portals?.let { portals ->
                    portals.open?.firstOrNull() ?: portals.webPortal?.firstOrNull()
                }
            )
        }
    }
}

/**
 * Describes basic information about an installed application.
 *
 * @property name The user-specified name given to the application.
 * @property version The currently installed version of the application.
 * @property iconUrl The URL of the application icon.
 * @property catalog The catalog this application was installed from.
 * @property train The catalog train this application was installed from.
 * @property state The current state of the application.
 * @property updateAvailable Whether the application has an update available.
 * @property webPortalUrl The URL to the applications web interface, if any.
 */
data class ApplicationOverview(
    val name: String,
    val version: String,
    val iconUrl: String,
    val catalog: String,
    val train: String,
    val state: State,
    val updateAvailable: Boolean,
    val webPortalUrl: String?
) {
    /**
     * Possible states an application may be in.
     */
    enum class State {
        DEPLOYING,
        ACTIVE,
        STOPPED
    }
}
