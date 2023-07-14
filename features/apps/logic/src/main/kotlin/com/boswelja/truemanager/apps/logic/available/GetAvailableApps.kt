package com.boswelja.truemanager.apps.logic.available

import com.boswelja.truemanager.core.api.v2.catalog.CatalogItems
import com.boswelja.truemanager.core.api.v2.catalog.CatalogV2Api
import com.boswelja.truemanager.core.api.v2.core.CoreV2Api
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
        var job = coreV2Api.getJob<CatalogItems>(jobId)
        while (job.state != "FINISHED") {
            job = coreV2Api.getJob(jobId)
            delay(JOB_POLLING_INTERVAL)
        }
        val items = requireNotNull(job.result) { "Failed getting items for $catalog" }
        return items.flatMap { (catalog, trains) ->
            trains.map { (train, item) ->
                AvailableApp(
                    name = item.name,
                    title = item.title,
                    version = item.latestHumanVersion,
                    iconUrl = item.iconUrl,
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
