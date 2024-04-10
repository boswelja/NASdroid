package com.nasdroid.api.v2.catalog

import com.nasdroid.api.TimestampUnwrapper
import com.nasdroid.api.exception.HttpNotOkException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Describes the TrueNAS API V2 "Catalog" group. The Catalogs API is responsible for managing
 * application catalogs, as well as getting their items.
 */
interface CatalogV2Api {

    /**
     * Retrieves a list of configured [Catalog]s on the system.
     *
     * @throws HttpNotOkException
     */
    suspend fun getCatalogs(): List<Catalog>

    /**
     * Creates a new catalog on the system.
     *
     * @return An ID for a running job. See [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun createCatalog(newCatalog: NewCatalog): Int

    /**
     * Get a catalog details by its [Catalog.id].
     *
     * @throws HttpNotOkException
     */
    suspend fun getCatalog(id: String): Catalog

    /**
     * Starts a job to retrieve items for a catalog by its [Catalog.id].
     *
     * @return An ID for a running job. See [com.nasdroid.api.v2.core.CoreV2Api.getJob]. The Job
     * returns [CatalogItems].
     *
     * @throws HttpNotOkException
     */
    suspend fun getCatalogItems(id: String, options: GetCatalogItemsOptions = GetCatalogItemsOptions()): Int

    /**
     * Retrieves details about a single catalog item.
     *
     * @param id The ID of the catalog item.
     * @param catalogName The name of the catalog that this item should exist in.
     * @param train The catalog train that this item should exist in.
     *
     * @throws HttpNotOkException
     */
    suspend fun getCatalogItemDetails(id: String, catalogName: String, train: String): CatalogItem

    /**
     * Delete a catalog by its [Catalog.id].
     *
     * @throws HttpNotOkException
     */
    suspend fun deleteCatalog(id: String)

    /**
     * Updates information for a catalog. It is expected that a catalog with the same ID already
     * exists.
     *
     * @throws HttpNotOkException
     */
    suspend fun updateCatalog(updatedCatalog: Catalog)

    /**
     * Updates the system item cache for all catalogs.
     *
     * @return An ID for a running job. See [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun syncAll(): Int

    /**
     * Updates the system item cache for the catalog with the given ID.
     *
     * @return An ID for a running job. See [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun syncCatalog(id: String): Int

    /**
     * Checks whether the configuration for the catalog with the given ID is valid.
     *
     * @return An ID for a running job. See [com.nasdroid.api.v2.core.CoreV2Api.getJob].
     *
     * @throws HttpNotOkException
     */
    suspend fun validateCatalog(id: String): Int
}

/**
 * Contains all available [CatalogItem]s in a catalog, grouped by the train that provides them.
 *
 * @property trainsToItems A Map of a catalog train to a list of [CatalogItem]s it provides.
 */
@JvmInline
@Serializable
value class CatalogItems(
    val trainsToItems: Map<String, Map<String, CatalogItem>>
)

/**
 * Describes aa catalog item available from a configured catalog.
 *
 * @property appReadme The HTML-formatted app README.
 * @property categories A list of categories this item belongs to.
 * @property description The app description.
 * @property healthy Whether the catalog entry is considered "healthy".
 * @property healthyError If not [healthy], this explains why.
 * @property homeUrl a URL pointing to the catalog items main website.
 * @property location The path where this catalog items chart information is stored.
 * @property latestVersion The latest available version of the catalog items container.
 * @property latestAppVersion The latest available version of the catalog items app.
 * @property latestHumanVersion A human-readable combination of [latestVersion] and
 * [latestAppVersion].
 * @property lastUpdate The date and time the last catalog update was published.
 * @property name The name of the catalog item.
 * @property recommended Whether the catalog item is recommended.
 * @property title The human-readable title of the catalog item.
 * @property maintainers A list of [Maintainer]s for the item.
 * @property tags A list of tags this catalog item has.
 * @property screenshotUrls A list of URLs for available screenshots of the app this item runs.
 * @property sourceUrls A list of URLs for available source code this catalog item uses.
 * @property iconUrl The URL of the catalog item icon.
 * @property versions Contains all metadata for available versions. TODO Main this.
 */
@Serializable
data class CatalogItem(
    @SerialName("app_readme")
    val appReadme: String,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("description")
    val description: String?,
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
    @Serializable(with = TimestampUnwrapper::class)
    @SerialName("last_update")
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
    val screenshotUrls: List<String>,
    @SerialName("sources")
    val sourceUrls: List<String>,
    @SerialName("icon_url")
    val iconUrl: String?,
    @SerialName("versions")
    val versions: JsonObject
) {

    /**
     * Describes a maintainer for a catalog item.
     *
     * @property name The full name of the maintainer.
     * @property url A URL to the maintainers website.
     * @property email The maintainers email address.
     */
    @Serializable
    data class Maintainer(
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String,
        @SerialName("email")
        val email: String
    )
}

/**
 * Describes a catalog present on the system.
 *
 * @property label  The name of the new catalog. This is displayed to the user, and can be changed
 * later.
 * @property repository The URL of the repository this catalog uses.
 * @property branch The branch of the repository.
 * @property builtin Whether the catalog is built-in to the system.
 * @property preferredTrains A list of preferred release trains.
 * @property location The path at which catalog data is stored.
 * @property id A unique identifier for this catalog.
 */
@Serializable
data class Catalog(
    @SerialName("label")
    val label: String,
    @SerialName("repository")
    val repository: String,
    @SerialName("branch")
    val branch: String,
    @SerialName("builtin")
    val builtin: Boolean,
    @SerialName("preferred_trains")
    val preferredTrains: List<String>,
    @SerialName("location")
    val location: String,
    @SerialName("id")
    val id: String,
)

/**
 * Holds the parameters needed to create a new catalog on the system.
 *
 * @property label The name of the new catalog. This is displayed to the user, and can be changed
 * later.
 * @property repository The URL of the repository this catalog uses.
 * @property branch The branch of the repository.
 * @property preferredTrains A list of preferred release trains.
 * @property force Whether creation of the catalog should be forced.
 */
@Serializable
data class NewCatalog(
    @SerialName("label")
    val label: String,
    @SerialName("repository")
    val repository: String,
    @SerialName("branch")
    val branch: String,
    @SerialName("preferred_trains")
    val preferredTrains: List<String>,
    @SerialName("force")
    val force: Boolean,
)

/**
 * Describes possible options for retrieving catalog items.
 *
 * @property cache Whether item details for a catalog come from cache, if available.
 * @property cacheOnly Force usage of cache for retrieving catalog information. If the content for
 * the catalog in question is not cached, no content would be returned. If [cache] is false, this
 * has no effect.
 * @property retrieveAllTrains Whether information for all the trains present in the catalog will be
 * retrieved.
 * @property trains A list of trains which will allow selective filtering to retrieve only
 * information of desired trains in a catalog. If [retrieveAllTrains] is set, this is ignored.
 */
@Serializable
data class GetCatalogItemsOptions(
    @SerialName("cache")
    val cache: Boolean= true,
    @SerialName("cache_only")
    val cacheOnly: Boolean = false,
    @SerialName("retrieve_all_trains")
    val retrieveAllTrains: Boolean = true,
    @SerialName("trains")
    val trains: List<String> = emptyList(),
)
