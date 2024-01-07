package com.nasdroid.apps.data.installed

import kotlinx.coroutines.flow.Flow

/**
 * Interface for accessing and managing installed apps in the cache.
 */
interface InstalledAppCache {

    /**
     * Get a flow of installed apps from the cache that match the given search term.
     *
     * @param searchTerm The search term to filter the installed apps by.
     * @return A flow of lists of [CachedInstalledApp] instances that match the search term.
     */
    fun getInstalledApps(
        searchTerm: String
    ): Flow<List<CachedInstalledApp>>

    /**
     * Asynchronously submit a list of installed apps to be cached.
     *
     * @param installedApps The list of [CachedInstalledApp] instances to cache.
     */
    suspend fun submitInstalledApps(installedApps: List<CachedInstalledApp>)

    /**
     * Asynchronously delete an installed app from the cache by name.
     *
     * @param appName The name of the [CachedInstalledApp] to delete.
     */
    suspend fun deleteInstalledApp(appName: String)

    /**
     * Asynchronously update the state of any cached app that matches the specified name.
     *
     * @param appName The name of the [CachedInstalledApp] whose state needs updating.
     * @param state The new state of the app.
     */
    suspend fun setState(appName: String, state: CachedInstalledApp.State)

    /**
     * Asynchronously set whether an update is available for any cached app that matches the
     * specified name.
     *
     * @param appName The name of the [CachedInstalledApp] whose state needs updating.
     * @param updateAvailable Whether the app has an update available.
     */
    suspend fun setUpdateAvailable(appName: String, updateAvailable: Boolean)
}
