package com.nasdroid.apps.data.installed

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import com.nasdroid.apps.data.AppsDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class InMemoryInstalledAppCacheTest {

    private lateinit var driver: JdbcSqliteDriver

    private lateinit var installedAppCache: InstalledAppCache

    @BeforeTest
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppsDatabase.Schema.create(driver)
        installedAppCache = InMemoryInstalledAppCache(AppsDatabase(driver))
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `getInstalledApps returns all apps when searchTerm is empty`() = runTest {
        installedAppCache.getInstalledApps("").test {
            assertEquals(emptyList<CachedInstalledApp>(), awaitItem())

            val expectedApps = listOf(
                CachedInstalledApp(
                    name = "Test App",
                    version = "1.0",
                    iconUrl = "icon url",
                    catalog = "catalog",
                    train = "train",
                    state = CachedInstalledApp.State.ACTIVE,
                    updateAvailable = true,
                    webPortalUrl = "https://google.com"
                )
            )
            installedAppCache.submitInstalledApps(expectedApps)

            assertEquals(expectedApps, awaitItem())
        }
    }

    @Test
    fun `submitInstalledApps caches expected apps`() = runTest {
        val installedApps = listOf(
            CachedInstalledApp(
                name = "Test App",
                version = "1.0",
                iconUrl = "icon url",
                catalog = "catalog",
                train = "train",
                state = CachedInstalledApp.State.ACTIVE,
                updateAvailable = true,
                webPortalUrl = "https://google.com"
            )
        )

        // Submit the installed apps to be cached
        installedAppCache.submitInstalledApps(installedApps)

        // Assert that the expected apps are in the cache
        installedAppCache.getInstalledApps("").first().also { actualApps ->
            assertEquals(1, actualApps.size)
            assertEquals("Test App", actualApps[0].name)
            assertEquals("1.0", actualApps[0].version)
        }
    }

    @Test
    fun `deleteInstalledApp removes expected app from cache`() = runTest {
        val appName = "Test App"
        installedAppCache.submitInstalledApps(
            listOf(
                CachedInstalledApp(
                    name = appName,
                    version = "1.0",
                    iconUrl = "icon url",
                    catalog = "catalog",
                    train = "train",
                    state = CachedInstalledApp.State.ACTIVE,
                    updateAvailable = true,
                    webPortalUrl = "https://google.com"
                )
            )
        )

        // Delete the app from the cache
        installedAppCache.deleteInstalledApp(appName)

        // Assert that the app is no longer in the cache
        assertEquals(emptyList<CachedInstalledApp>(), installedAppCache.getInstalledApps("").first())
    }

    @Test
    fun `setState sets the state of the app in cache`() = runTest {
        val apps = listOf(
            CachedInstalledApp(
                name = "Test App",
                version = "1.0",
                iconUrl = "icon url",
                catalog = "catalog",
                train = "train",
                state = CachedInstalledApp.State.ACTIVE,
                updateAvailable = true,
                webPortalUrl = "https://google.com"
            )
        )
        installedAppCache.submitInstalledApps(apps)

        installedAppCache.getInstalledApps("").test {
            assertEquals(apps, awaitItem())

            val newState = CachedInstalledApp.State.STOPPED
            apps.forEach {
                installedAppCache.setState(it.name, newState)
            }
            val appsWithUpdatedStates = apps.map { it.copy(state = newState) }

            assertEquals(appsWithUpdatedStates, awaitItem())
        }
    }

    @Test
    fun `setUpdateAvailable sets the updateAvailable of the app in cache`() = runTest {
        val apps = listOf(
            CachedInstalledApp(
                name = "Test App",
                version = "1.0",
                iconUrl = "icon url",
                catalog = "catalog",
                train = "train",
                state = CachedInstalledApp.State.ACTIVE,
                updateAvailable = true,
                webPortalUrl = "https://google.com"
            )
        )
        installedAppCache.submitInstalledApps(apps)

        installedAppCache.getInstalledApps("").test {
            assertEquals(apps, awaitItem())

            val newState = false
            apps.forEach {
                installedAppCache.setUpdateAvailable(it.name, newState)
            }
            val appsWithUpdatedStates = apps.map { it.copy(updateAvailable = newState) }

            assertEquals(appsWithUpdatedStates, awaitItem())
        }
    }
}
