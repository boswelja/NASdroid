package com.nasdroid.api.v2.app

import com.nasdroid.api.TimestampUnwrapper
import com.nasdroid.api.exception.HttpNotOkException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API V2 "App" group. The App APIs are responsible for retrieving applications
 * that the user can install.
 */
interface AppV2Api {

    /**
     * Retrieve a list of all available apps from all catalogs.
     *
     * @throws HttpNotOkException
     */
    suspend fun getAvailable(): List<AvailableApp>

    /**
     * Retrieve a list of categories for all available apps. Note catalogs define their own
     * categories.
     *
     * @throws HttpNotOkException
     */
    suspend fun getCategories(): List<String>

    /**
     * Retrieve a list of available apps that have been recently updated.
     *
     * @throws HttpNotOkException
     */
    suspend fun getLatest(): List<AvailableApp>

    /**
     * Retrieve a list of apps that are similar to the specified app.
     *
     * @param appName The name of the app whose related apps should be retrieved.
     * @param catalog The catalog which the app belongs to.
     * @param train The catalog train which the app belongs to.
     *
     * @throws HttpNotOkException
     */
    suspend fun getSimilarTo(appName: String, catalog: String, train: String): List<AvailableApp>
}

@Serializable
data class AvailableApp(
    @SerialName("catalog")
    val catalog: String,
    @SerialName("installed")
    val installed: Boolean,
    @SerialName("train")
    val train: String,
    @SerialName("app_readme")
    val htmlReadme: String,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("description")
    val description: String,
    @SerialName("healthy")
    val healthy: Boolean,
    @SerialName("healthy_error")
    val healthyError: String?,
    @SerialName("home")
    val homeUrl: String,
    @SerialName("location")
    val location: String,
    @SerialName("latest_version")
    val latestVersion: String,
    @SerialName("latest_app_version")
    val latestAppVersion: String,
    @SerialName("latest_human_version")
    val latestHumanVersion: String,
    @SerialName("last_update")
    @Serializable(TimestampUnwrapper::class)
    val lastUpdate: Long,
    @SerialName("name")
    val name: String,
    @SerialName("recommended")
    val recommended: Boolean,
    @SerialName("title")
    val title: String,
    @SerialName("maintainers")
    val maintainers: List<Maintainer>,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("screenshots")
    val screenshots: List<String>,
    @SerialName("sources")
    val sources: List<String>,
    @SerialName("icon_url")
    val iconUrl: String,
    @SerialName("versions")
    val versions: String?,
) {

    @Serializable
    data class Maintainer(
        @SerialName("email")
        val email: String,
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String,
    )
}
