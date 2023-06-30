package com.boswelja.truemanager.core.api.v2.catalog

/**
 * Describes the TrueNAS API V2 "Catalog" group. The Catalogs API is responsible for managing
 * application catalogs, as well as getting their items.
 */
interface CatalogV2Api {

    /**
     * Retrieves a list of configured [Catalog]s on the system.
     */
    suspend fun getCatalogs(): List<Catalog>

    /**
     * Creates a new catalog on the system.
     *
     * @return An ID for a running job. TODO Job system.
     */
    suspend fun createCatalog(newCatalog: NewCatalog): Int

    /**
     * Get a catalog details by its [Catalog.id].
     */
    suspend fun getCatalog(id: String): Catalog

    /**
     * Starts a job to retrieve items for a catalog by its [Catalog.id].
     *
     * @return An ID for a running job. TODO Job system.
     */
    suspend fun getCatalogItems(id: String, options: GetCatalogItemsOptions): Int

    /**
     * Delete a catalog by its [Catalog.id].
     */
    suspend fun deleteCatalog(id: String)

    /**
     * Updates information for a catalog. It is expected that a catalog with the same ID already
     * exists.
     */
    suspend fun updateCatalog(updatedCatalog: Catalog)

    /**
     * Updates the system item cache for all catalogs.
     *
     * @return An ID for a running job. TODO Job system.
     */
    suspend fun syncAll(): Int

    /**
     * Updates the system item cache for the catalog with the given ID.
     *
     * @return An ID for a running job. TODO Job system.
     */
    suspend fun syncCatalog(id: String): Int

    /**
     * Checks whether the configuration for the catalog with the given ID is valid.
     *
     * @return An ID for a running job. TODO Job system.
     */
    suspend fun validateCatalog(id: String): Int
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
data class Catalog(
    val label: String,
    val repository: String,
    val branch: String,
    val builtin: Boolean,
    val preferredTrains: List<String>,
    val location: String,
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
data class NewCatalog(
    val label: String,
    val repository: String,
    val branch: String,
    val preferredTrains: List<String>,
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
data class GetCatalogItemsOptions(
    val cache: Boolean= true,
    val cacheOnly: Boolean = false,
    val retrieveAllTrains: Boolean = true,
    val trains: List<String> = emptyList(),
)
