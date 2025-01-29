package com.nasdroid.apps.logic.discover

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.v2.catalog.CatalogV2Api

/**
 * Retrieves a list of catalogs that are providing apps to the user. See [invoke] for details.
 */
class GetAvailableCatalogs(
    private val catalogV2Api: CatalogV2Api
) {

    /**
     * Get a list of Strings representing user-defined catalog labels.
     */
    suspend operator fun invoke(): Result<List<String>> {
        return try {
            val categories = catalogV2Api.getCatalogs()
                .map { it.label }
            Result.success(categories)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}
