package com.nasdroid.apps.logic.available

import com.nasdroid.api.v2.catalog.CatalogItems
import com.nasdroid.api.v2.catalog.CatalogV2Api
import com.nasdroid.api.v2.core.CoreV2Api
import com.nasdroid.api.v2.core.getJob
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/**
 * Gets a list of apps available from a single catalog. See [invoke] for details.
 */
class GetAvailableApps(
    private val catalogV2Api: CatalogV2Api,
    private val coreV2Api: CoreV2Api,
) {

    /**
     * Get a list of [AvailableApp]s from the catalog with the given name.
     */
    suspend operator fun invoke(catalog: String): List<AvailableApp> {
        val jobId = catalogV2Api.getCatalogItems(catalog)
        var result: CatalogItems? = null
        while (result == null) {
            delay(JOB_POLLING_INTERVAL)
            result = coreV2Api.getJob<CatalogItems>(jobId).result
        }
        return result.trainsToItems.flatMap { (train, items) ->
            items.map { (_, item) ->
                AvailableApp(
                    name = item.name,
                    title = item.title,
                    version = item.latestHumanVersion,
                    iconUrl = item.iconUrl ?: "",
                    catalog = catalog,
                    train = train,
                )
            }
        }
    }

    companion object {
        private val JOB_POLLING_INTERVAL = 250.milliseconds
    }
}

/**
 * Describes an application available from a catalog.
 *
 * @property name The application name. This functions as a unique identifier for the application in
 * the catalog.
 * @property title The human-readable title of the application.
 * @property version The latest version of the application.
 * @property iconUrl A URL for the application icon.
 * @property catalog The name of the catalog this application belongs to.
 * @property train The catalog train this application belongs to.
 */
data class AvailableApp(
    val name: String,
    val title: String,
    val version: String,
    val iconUrl: String,
    val catalog: String,
    val train: String,
)
