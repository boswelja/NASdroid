package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.chart.release.ChartRelease
import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import kotlinx.datetime.Instant

/**
 * Retrieves details about a single app installed on the system. See [invoke] for details.
 */
class GetInstalledApp(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Get a single [InstalledAppDetails] for the app whose name matches [appName].
     */
    suspend operator fun invoke(appName: String): Result<InstalledAppDetails> {
        return try {
            val chartRelease = chartReleaseV2Api.getChartRelease(appName)
            Result.success(
                InstalledAppDetails(
                    name = chartRelease.id,
                    iconUrl = chartRelease.chartMetadata.icon,
                    notes = chartRelease.info.notes,
                    appVersion = chartRelease.chartMetadata.appVersion,
                    chartVersion = chartRelease.chartMetadata.version,
                    lastUpdated = null, // TODO
                    sources = chartRelease.chartMetadata.sources.orEmpty(),
                    developer = null, // TODO
                    catalog = chartRelease.catalog,
                    train = chartRelease.catalogTrain,
                    state = when (chartRelease.status) {
                        ChartRelease.Status.DEPLOYING -> InstalledAppDetails.State.DEPLOYING
                        ChartRelease.Status.ACTIVE -> InstalledAppDetails.State.ACTIVE
                        ChartRelease.Status.STOPPED -> InstalledAppDetails.State.STOPPED
                    },
                    hasUpdateAvailable = chartRelease.updateAvailable,
                    webPortalUrl = chartRelease.portals?.webPortal?.firstOrNull()
                )
            )
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}

/**
 * Describes basic information about an installed application.
 *
 * @property name The user-specified name given to the application.
 * @property iconUrl The URL of the application icon.
 * @property notes A Markdown-formatted block of text used as short documentation for the app.
 * @property appVersion The version of the app that is installed in the container.
 * @property chartVersion The version of the container the app is installed in.
 * @property lastUpdated Indicates when the last update was installed, if possible.
 * @property sources A list of URLs linking to various sources used in the app.
 * @property developer The details of the app maintainer, if available.
 * @property catalog The catalog this application was installed from.
 * @property train The catalog train this application was installed from.
 * @property state The current state of the application.
 * @property hasUpdateAvailable Whether the application has an update available.
 * @property webPortalUrl The URL to the applications web interface, if any.
 */
data class InstalledAppDetails(
    val name: String,
    val iconUrl: String,
    val notes: String?,
    val appVersion: String,
    val chartVersion: String,
    val lastUpdated: Instant?,
    val sources: List<String>,
    val developer: String?,
    val catalog: String,
    val train: String,
    val state: State,
    val hasUpdateAvailable: Boolean,
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
