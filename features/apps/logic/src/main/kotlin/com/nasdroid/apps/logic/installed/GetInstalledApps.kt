package com.nasdroid.apps.logic.installed

import com.nasdroid.api.v2.chart.release.ChartRelease
import com.nasdroid.api.v2.chart.release.ChartReleaseV2Api
import com.nasdroid.apps.data.installed.CachedInstalledApp
import com.nasdroid.apps.data.installed.InstalledAppCache
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

/**
 * Gets a list of applications installed on the system. See [invoke] for details.
 */
class GetInstalledApps(
    private val chartReleaseV2Api: ChartReleaseV2Api,
    private val installedAppCache: InstalledAppCache,
    private val clock: Clock,
) {

    private var lastSearchQuery: String? = null
    private var lastCacheRefresh = Instant.DISTANT_PAST

    /**
     * Gets a list of all applications installed on the system.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(searchTerm: String): Flow<List<InstalledAppOverview>> {
        return installedAppCache.getInstalledApps(searchTerm)
            .onStart {
                val queryTime = clock.now()
                val isNewQuery = (lastSearchQuery != searchTerm)
                val isCacheStale = (queryTime - lastCacheRefresh) > CacheValidityTimer
                if (!isNewQuery || isCacheStale) {
                    val releaseDtos = chartReleaseV2Api.getChartReleases()
                    installedAppCache.submitInstalledApps(
                        releaseDtos.map { installedApp ->
                            installedApp.toCachedApp()
                        }
                    )
                    lastCacheRefresh = queryTime
                }
                lastSearchQuery = searchTerm
            }
            .mapLatest { cachedInstalledApps ->
                cachedInstalledApps.map { cachedApp ->
                    InstalledAppOverview(cachedApp)
                }.sortedBy { app -> app.name }
            }
    }

    private fun ChartRelease.toCachedApp(): CachedInstalledApp {
        return CachedInstalledApp(
            name = id,
            version = humanVersion,
            iconUrl = chartMetadata.icon,
            catalog = catalog,
            train = catalogTrain,
            state = when (status) {
                ChartRelease.Status.DEPLOYING -> CachedInstalledApp.State.DEPLOYING
                ChartRelease.Status.ACTIVE -> CachedInstalledApp.State.ACTIVE
                ChartRelease.Status.STOPPED -> CachedInstalledApp.State.STOPPED
            },
            hasUpdateAvailable = updateAvailable,
            webPortalUrl = portals?.let { portals ->
                portals.open?.firstOrNull() ?: portals.webPortal?.firstOrNull()
            }
        )
    }

    companion object {
        private val CacheValidityTimer = 1.minutes
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
 * @property hasUpdateAvailable Whether the application has an update available.
 * @property webPortalUrl The URL to the applications web interface, if any.
 */
data class InstalledAppOverview(
    val name: String,
    val version: String,
    val iconUrl: String,
    val catalog: String,
    val train: String,
    val state: State,
    val hasUpdateAvailable: Boolean,
    val webPortalUrl: String?
) {
    internal constructor(cachedApp: CachedInstalledApp) : this(
        name = cachedApp.name,
        version = cachedApp.version,
        iconUrl = cachedApp.iconUrl,
        catalog = cachedApp.catalog,
        train = cachedApp.train,
        state = when (cachedApp.state) {
            CachedInstalledApp.State.ACTIVE -> State.ACTIVE
            CachedInstalledApp.State.STOPPED -> State.STOPPED
            CachedInstalledApp.State.DEPLOYING -> State.DEPLOYING
        },
        hasUpdateAvailable = cachedApp.hasUpdateAvailable,
        webPortalUrl = cachedApp.webPortalUrl
    )

    /**
     * Possible states an application may be in.
     */
    enum class State {
        DEPLOYING,
        ACTIVE,
        STOPPED
    }
}
