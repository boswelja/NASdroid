package com.nasdroid.api.v2.app

import com.nasdroid.api.v2.TimestampUnwrapper
import com.nasdroid.api.v2.exception.HttpNotOkException
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

/**
 * Describes an application available to be installed on the system.
 *
 * @property catalog The catalog this app belongs to.
 * @property installed Whether an instance of this app is already installed.
 * @property train The catalog train this app belongs to.
 * @property htmlReadme The app README, using HTML formatting.
 * @property categories A list of categories this app belongs to.
 * @property description A brief overview of what the app is.
 * @property healthy Whether the app is considered "healthy".
 * @property healthyError When not [healthy], this contains the reason.
 * @property homeUrl A URL for the homepage of the app.
 * @property location The location on the system where the app information is stored.
 * @property latestVersion The latest version of the container the app runs in.
 * @property latestAppVersion The latest version of the software that runs inside the container.
 * @property latestHumanVersion A human-readable version that incorporates both [latestVersion] and
 * [latestAppVersion].
 * @property lastUpdate The timestamp in milliseconds since epoch of the last app update.
 * @property name The name of the app.
 * @property recommended Whether the system recommends this app to the user.
 * @property title The human-readable title of the app.
 * @property maintainers A list of active [maintainers].
 * @property tags A list of tags this app has.
 * @property screenshots A list of URLs for screenshots of this app. This may be empty.
 * @property sources A list of URLs pointing to the app source code.
 * @property iconUrl A URL for the app icon.
 * @property versions TODO This is a JSON object that is empty in all of my test data.
 */
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
    val iconUrl: String?,
    @SerialName("versions")
    val versions: Map<String, String>?,
) {

    /**
     * Describes a maintainer for an available app.
     *
     * @property email The maintainers email address.
     * @property name The name of the maintainer.
     * @property url A URL for the maintainers preferred homepage.
     */
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
