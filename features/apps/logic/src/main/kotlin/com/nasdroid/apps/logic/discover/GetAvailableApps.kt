package com.nasdroid.apps.logic.discover

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.app.AppV2Api
import com.nasdroid.apps.logic.discover.AvailableApp.Companion.toSanitizedModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Retrieves a list of [SortedApps] that can be installed on the system. See [invoke] for details.
 */
class GetAvailableApps(
    private val appV2Api: AppV2Api
) {

    /**
     * Retrieves a list of [SortedApps] that can be installed on the system. Sorting and filtering
     * are controlled by the parameters passed to this function.
     *
     * @param searchQuery A search query the user has entered. This is checked against app titles
     * and descriptions in a case-insensitive fashion.
     * @param sortMode The desired sorting mode for the items. See [SortMode].
     * @param filteredCatalogs A list of catalogs whose apps should be included from the result.
     * An empty list means all catalogs will be returned. Note this is not different from a list
     * containing all catalogs.
     * @param filteredCategories A list of categories whose apps should be returned in the result.
     * An empty list means all categories will be returned. Note this is not different from a list
     * containing all categories.
     */
    suspend operator fun invoke(
        searchQuery: String,
        sortMode: SortMode,
        filteredCatalogs: List<String>,
        filteredCategories: List<String>,
    ): Result<List<SortedApps>> {
        val availableApps = try {
            appV2Api.getAvailable()
                .filter { !it.categories.contains("generic") }
                .filterByCatalogs(filteredCatalogs)
                .filterByCategories(filteredCategories)
                .filterBySearch(searchQuery)
        } catch (e: HttpNotOkException) {
            return Result.failure(e)
        }

        val sortedApps = when (sortMode) {
            SortMode.Category -> {
                val categoriesToApps = mutableMapOf<String, List<AvailableApp>>()
                availableApps.forEach { app ->
                    val sanitizedApp = app.toSanitizedModel()
                    app.categories.forEach { category ->
                        categoriesToApps.compute(category) { _, currentList ->
                            if (currentList == null) {
                                listOf(sanitizedApp)
                            } else {
                                currentList + sanitizedApp
                            }
                        }
                    }
                }
                categoriesToApps
                    .map { (category, apps) -> SortedApps(category, apps) }
                    .sortedBy { it.groupTitle }
            }
            SortMode.AppName -> {
                availableApps
                    .map { it.toSanitizedModel() }
                    .groupBy { it.title.first().uppercase() }
                    .map { (category, apps) -> SortedApps(category, apps) }
                    .sortedBy { it.groupTitle }
            }
            SortMode.CatalogName -> {
                availableApps
                    .map { it.toSanitizedModel() }
                    .groupBy { it.catalogName }
                    .map { (category, apps) -> SortedApps(category, apps) }
                    .sortedBy { it.groupTitle }
            }
            SortMode.UpdatedDate -> {
                availableApps
                    .map { it.toSanitizedModel() }
                    .sortedByDescending { it.lastUpdated }
                    .groupBy {
                        it.lastUpdated.toLocalDateTime(TimeZone.currentSystemDefault()).date
                    }
                    .map { (category, apps) ->
                        SortedApps(category.toString(), apps)
                    }
            }
        }

        return Result.success(sortedApps)
    }

    private fun List<com.nasdroid.api.v2.app.AvailableApp>.filterByCatalogs(
        catalogs: List<String>
    ): List<com.nasdroid.api.v2.app.AvailableApp> {
        return if (catalogs.isNotEmpty()) {
            filter { catalogs.contains(it.catalog) }
        } else {
            this
        }
    }

    private fun List<com.nasdroid.api.v2.app.AvailableApp>.filterByCategories(
        categories: List<String>
    ): List<com.nasdroid.api.v2.app.AvailableApp> {
        return if (categories.isNotEmpty()) {
            filter { app -> app.categories.any { categories.contains(it) } }
        } else {
            this
        }
    }

    private fun List<com.nasdroid.api.v2.app.AvailableApp>.filterBySearch(
        searchQuery: String
    ): List<com.nasdroid.api.v2.app.AvailableApp> {
        return if (searchQuery.isNotEmpty()) {
            filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }
        } else {
            this
        }
    }
}

/**
 * All possible sorting modes for [GetAvailableApps].
 */
enum class SortMode {

    /**
     * Apps are grouped by category. One app can have many categories, and thus can exist in
     * multiple sorted groups at once in this mode. Categories themselves are sorted alphabetically.
     */
    Category,

    /**
     * Apps are sorted alphabetically by their titles.
     */
    AppName,

    /**
     * Apps are grouped by the catalog that contains them. Catalogs themselves are sorted
     * alphabetically.
     */
    CatalogName,

    /**
     * Apps are grouped by the date they were last updated.
     */
    UpdatedDate,
}

/**
 * Describes a list of [AvailableApp]s that have been sorted into a group.
 *
 * @property groupTitle The title of the group the apps are sorted into.
 * @property apps The list of apps in this group.
 */
data class SortedApps(
    val groupTitle: String,
    val apps: List<AvailableApp>
)

