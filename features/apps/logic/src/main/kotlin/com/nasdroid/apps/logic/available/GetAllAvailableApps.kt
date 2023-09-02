package com.nasdroid.apps.logic.available

import com.nasdroid.api.v2.catalog.CatalogV2Api
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Gets a list of available applications across all configured catalogs. See [invoke] for details.
 */
class GetAllAvailableApps(
    private val catalogV2Api: CatalogV2Api,
    private val getAvailableApps: GetAvailableApps,
) {

    /**
     * Get a list of [AvailableApp]s from every registered catalog.
     */
    suspend operator fun invoke(): List<AvailableApp> = coroutineScope {
        val catalogs = catalogV2Api.getCatalogs()

        val asyncTasks = catalogs.map { catalog ->
            async {
                getAvailableApps(catalog.id)
            }
        }

        asyncTasks.awaitAll().flatten()
    }
}
