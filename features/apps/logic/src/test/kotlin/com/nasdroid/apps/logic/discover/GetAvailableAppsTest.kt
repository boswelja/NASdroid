package com.nasdroid.apps.logic.discover

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.app.AppV2Api
import com.nasdroid.api.v2.app.AvailableApp as ApiAvailableApp
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetAvailableAppsTest {

    private lateinit var mockAppV2Api: AppV2Api

    private lateinit var getAvailableApps: GetAvailableApps

    @BeforeTest
    fun setUp() {
        mockAppV2Api = mockk()

        getAvailableApps = GetAvailableApps(mockAppV2Api)
    }

    @Test
    fun `when HttpNotOkException thrown, error is returned`() = runTest {
        val exception = HttpNotOkException(418, "I'm a teapot")
        coEvery { mockAppV2Api.getAvailable() } throws exception

        val result = getAvailableApps("", SortMode.CatalogName, emptyList(), emptyList())

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `when SortMode Category, success is returned`() = runTest {
        coEvery { mockAppV2Api.getAvailable() } returns MockApiResponse

        val result = getAvailableApps("", SortMode.Category, emptyList(), emptyList())

        assertTrue(result.isSuccess)
        assertEquals(MockExpectedResultByCategory, result.getOrNull())
    }

    @Test
    fun `when SortMode Name, success is returned`() = runTest {
        coEvery { mockAppV2Api.getAvailable() } returns MockApiResponse

        val result = getAvailableApps("", SortMode.AppName, emptyList(), emptyList())

        assertTrue(result.isSuccess)
        assertEquals(MockExpectedResultByName, result.getOrNull())
    }

    @Test
    fun `when SortMode Catalog, success is returned`() = runTest {
        coEvery { mockAppV2Api.getAvailable() } returns MockApiResponse

        val result = getAvailableApps("", SortMode.CatalogName, emptyList(), emptyList())

        assertTrue(result.isSuccess)
        assertEquals(MockExpectedResultByCatalog, result.getOrNull())
    }

    @Test
    fun `when searching, success is returned`() = runTest {
        coEvery { mockAppV2Api.getAvailable() } returns MockApiResponse

        val result = getAvailableApps("SearXNG", SortMode.Category, emptyList(), emptyList())

        assertTrue(result.isSuccess)
        // SearXNG is in the productivity category, so we can drop all categories except that
        assertEquals(
            MockExpectedResultByCategory.filter { it.groupTitle == "productivity" },
            result.getOrNull()
        )
    }

    companion object {
        @Suppress("MaxLineLength")
        private val MockApiResponse = listOf(
            ApiAvailableApp(
                catalog = "TRUECHARTS",
                installed = false,
                train = "enterprise",
                htmlReadme = "<p>Unofficial Bitwarden compatible server written in Rust</p>\\n<p>This App is supplied by TrueCharts, for more information visit the manual: <a href=\\\"https://truecharts.org/charts/enterprise/vaultwarden\\\">https://truecharts.org/charts/enterprise/vaultwarden</a></p>\\n<hr />\\n<p>TrueCharts can only exist due to the incredible effort of our staff.\\nPlease consider making a <a href=\\\"https://truecharts.org/sponsor\\\">donation</a> or contributing back to the project any way you can!</p>",
                categories = listOf("security"),
                description = "Unofficial Bitwarden compatible server written in Rust",
                healthy = true,
                healthyError = null,
                homeUrl = "https://truecharts.org/charts/enterprise/vaultwarden",
                location = "/mnt/ix-applications/catalogs/github_com_truecharts_catalog_main/enterprise/vaultwarden",
                latestVersion = "23.0.11",
                latestAppVersion = "1.30.1",
                latestHumanVersion = "1.30.1_23.0.11",
                lastUpdate = 1700853190000,
                name = "vaultwarden",
                recommended = false,
                title = "Vaultwarden",
                maintainers = listOf(
                    ApiAvailableApp.Maintainer("info@truecharts.org", "TrueCharts", "https://truecharts.org")
                ),
                tags = emptyList(),
                screenshots = emptyList(),
                sources = listOf(
                    "https://github.com/truecharts/charts/tree/master/charts/enterprise/vaultwarden",
                    "https://github.com/dani-garcia/vaultwarden"
                ),
                iconUrl = "https://truecharts.org/img/hotlink-ok/chart-icons/vaultwarden.png",
                versions = null
            ),
            ApiAvailableApp(
                catalog = "TRUENAS",
                installed = false,
                train = "community",
                htmlReadme = "<h1>SearXNG</h1>\\n<p><a href=\\\"https://github.com/searxng/searxng\\\">SearXNG</a> is a privacy-respecting, hackable metasearch engine</p>",
                categories = listOf("productivity"),
                description = "SearXNG is a privacy-respecting, hackable metasearch engine",
                healthy = true,
                healthyError = null,
                homeUrl = "https://github.com/searxng/searxng",
                location = "/mnt/ix-applications/catalogs/github_com_truenas_charts_git_master/community/searxng",
                latestVersion = "1.2.0",
                latestAppVersion = "2023.11.23",
                latestHumanVersion = "2023.11.23_1.2.0",
                lastUpdate = 1700814844000,
                name = "searxng",
                recommended = false,
                title = "SearXNG",
                maintainers = listOf(
                    ApiAvailableApp.Maintainer("dev@ixsystems.com", "truenas", "https://www.truenas.com/")
                ),
                tags = listOf("search"),
                screenshots = emptyList(),
                sources = listOf(
                    "https://hub.docker.com/r/searxng/searxng",
                    "https://github.com/truenas/charts/tree/master/library/ix-dev/community/searxng",
                    "https://github.com/searxng/searxng"
                ),
                iconUrl = "https://media.sys.truenas.net/apps/searxng/icons/icon.svg",
                versions = null
            ),
            ApiAvailableApp(
                catalog = "TRUENAS",
                installed = false,
                train = "charts",
                htmlReadme = "<h1>Photo Prism</h1>\\n<p>PhotoPrism is a server-based application for browsing, organizing and sharing your personal photo collection.</p>",
                categories = listOf("media"),
                description = "AI-powered app for browsing, organizing & sharing your photo collection.",
                healthy = true,
                healthyError = null,
                homeUrl = "https://photoprism.app/",
                location = "/mnt/ix-applications/catalogs/github_com_truenas_charts_git_master/charts/photoprism",
                latestVersion = "1.0.32",
                latestAppVersion = "231021",
                latestHumanVersion = "231021_1.0.32",
                lastUpdate = 1700814767000,
                name = "photoprism",
                recommended = false,
                title = "PhotoPrism",
                maintainers = listOf(
                    ApiAvailableApp.Maintainer("dev@ixsystems.com", "truenas", "https://www.truenas.com/")
                ),
                tags = listOf(
                    "photos",
                    "image"
                ),
                screenshots = listOf(
                    "https://media.sys.truenas.net/apps/photoprism/screenshots/screenshot1.png",
                    "https://media.sys.truenas.net/apps/photoprism/screenshots/screenshot2.png"
                ),
                sources = listOf(
                    "https://photoprism.app/",
                    "https://github.com/truenas/charts/tree/master/charts/photoprism"
                ),
                iconUrl = "https://media.sys.truenas.net/apps/photoprism/icons/icon.svg",
                versions = null
            )
        )

        private val MockExpectedResultByCategory = listOf(
            SortedApps(
                "media",
                listOf(
                    AvailableApp(
                        id = "photoprism",
                        title = "PhotoPrism",
                        description = "AI-powered app for browsing, organizing & sharing your photo collection.",
                        iconUrl = "https://media.sys.truenas.net/apps/photoprism/icons/icon.svg",
                        version = "231021",
                        catalogName = "TRUENAS",
                        catalogTrain = "charts",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814767000)
                    )
                )
            ),
            SortedApps(
                "productivity",
                listOf(
                    AvailableApp(
                        id = "searxng",
                        title = "SearXNG",
                        description = "SearXNG is a privacy-respecting, hackable metasearch engine",
                        iconUrl = "https://media.sys.truenas.net/apps/searxng/icons/icon.svg",
                        version = "2023.11.23",
                        catalogName = "TRUENAS",
                        catalogTrain = "community",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814844000)
                    )
                )
            ),
            SortedApps(
                "security",
                listOf(
                    AvailableApp(
                        id = "vaultwarden",
                        title = "Vaultwarden",
                        description = "Unofficial Bitwarden compatible server written in Rust",
                        iconUrl = "https://truecharts.org/img/hotlink-ok/chart-icons/vaultwarden.png",
                        version = "1.30.1",
                        catalogName = "TRUECHARTS",
                        catalogTrain = "enterprise",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700853190000)
                    )
                )
            ),
        )

        private val MockExpectedResultByName = listOf(
            SortedApps(
                "P",
                listOf(
                    AvailableApp(
                        id = "photoprism",
                        title = "PhotoPrism",
                        description = "AI-powered app for browsing, organizing & sharing your photo collection.",
                        iconUrl = "https://media.sys.truenas.net/apps/photoprism/icons/icon.svg",
                        version = "231021",
                        catalogName = "TRUENAS",
                        catalogTrain = "charts",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814767000)
                    )
                )
            ),
            SortedApps(
                "S",
                listOf(
                    AvailableApp(
                        id = "searxng",
                        title = "SearXNG",
                        description = "SearXNG is a privacy-respecting, hackable metasearch engine",
                        iconUrl = "https://media.sys.truenas.net/apps/searxng/icons/icon.svg",
                        version = "2023.11.23",
                        catalogName = "TRUENAS",
                        catalogTrain = "community",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814844000)
                    )
                )
            ),
            SortedApps(
                "V",
                listOf(
                    AvailableApp(
                        id = "vaultwarden",
                        title = "Vaultwarden",
                        description = "Unofficial Bitwarden compatible server written in Rust",
                        iconUrl = "https://truecharts.org/img/hotlink-ok/chart-icons/vaultwarden.png",
                        version = "1.30.1",
                        catalogName = "TRUECHARTS",
                        catalogTrain = "enterprise",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700853190000)
                    )
                )
            ),
        )

        private val MockExpectedResultByCatalog = listOf(
            SortedApps(
                "TRUECHARTS",
                listOf(
                    AvailableApp(
                        id = "vaultwarden",
                        title = "Vaultwarden",
                        description = "Unofficial Bitwarden compatible server written in Rust",
                        iconUrl = "https://truecharts.org/img/hotlink-ok/chart-icons/vaultwarden.png",
                        version = "1.30.1",
                        catalogName = "TRUECHARTS",
                        catalogTrain = "enterprise",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700853190000)
                    )
                )
            ),
            SortedApps(
                "TRUENAS",
                listOf(
                    AvailableApp(
                        id = "searxng",
                        title = "SearXNG",
                        description = "SearXNG is a privacy-respecting, hackable metasearch engine",
                        iconUrl = "https://media.sys.truenas.net/apps/searxng/icons/icon.svg",
                        version = "2023.11.23",
                        catalogName = "TRUENAS",
                        catalogTrain = "community",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814844000)
                    ),

                    AvailableApp(
                        id = "photoprism",
                        title = "PhotoPrism",
                        description = "AI-powered app for browsing, organizing & sharing your photo collection.",
                        iconUrl = "https://media.sys.truenas.net/apps/photoprism/icons/icon.svg",
                        version = "231021",
                        catalogName = "TRUENAS",
                        catalogTrain = "charts",
                        isInstalled = false,
                        lastUpdated = Instant.fromEpochMilliseconds(1700814767000)
                    ),
                )
            ),
        )
    }
}
