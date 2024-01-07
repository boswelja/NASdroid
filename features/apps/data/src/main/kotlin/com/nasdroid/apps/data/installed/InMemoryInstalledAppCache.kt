package com.nasdroid.apps.data.installed

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.nasdroid.apps.data.AppsDatabase
import com.nasdroid.apps.data.CachedInstalledApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

/**
 * An implementation of [InstalledAppCache] that is backed by an in-memory database.
 */
class InMemoryInstalledAppCache(
    private val database: AppsDatabase
) : InstalledAppCache {

    private val queries
        get() = database.cachedInstalledAppsQueries

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getInstalledApps(searchTerm: String): Flow<List<CachedInstalledApp>> {
        return if (searchTerm.isBlank()) {
            queries.getAll()
        } else {
            queries.searchAll(searchTerm)
        }
            .asFlow()
            .mapToList(Dispatchers.IO)
            .mapLatest {
                it.map { cachedApp ->
                    CachedInstalledApp(
                        name = cachedApp.app_name,
                        version = cachedApp.version,
                        iconUrl = cachedApp.icon_url,
                        catalog = cachedApp.catalog,
                        train = cachedApp.train,
                        state = CachedInstalledApp.State.valueOf(cachedApp.state),
                        updateAvailable = cachedApp.update_available,
                        webPortalUrl = cachedApp.web_portal_url
                    )
                }
        }
    }

    override suspend fun submitInstalledApps(installedApps: List<CachedInstalledApp>) {
        withContext(Dispatchers.IO) {
            queries.transaction {
                queries.deleteAll()
                installedApps.forEach {
                    queries.insert(
                        CachedInstalledApps(
                            app_name = it.name,
                            version = it.version,
                            icon_url = it.iconUrl,
                            catalog = it.catalog,
                            train = it.train,
                            state = it.state.name,
                            update_available = it.updateAvailable,
                            web_portal_url = it.webPortalUrl
                        )
                    )
                }
            }
        }
    }

    override suspend fun deleteInstalledApp(appName: String) {
        withContext(Dispatchers.IO) {
            queries.deleteOne(appName)
        }
    }

    override suspend fun setState(appName: String, state: CachedInstalledApp.State) {
        withContext(Dispatchers.IO) {
            queries.updateState(state = state.name, app_name = appName)
        }
    }

    override suspend fun setUpdateAvailable(appName: String, updateAvailable: Boolean) {
        withContext(Dispatchers.IO) {
            queries.updateUpdateAvailable(update_available = updateAvailable, app_name = appName)
        }
    }
}
