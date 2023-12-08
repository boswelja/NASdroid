package com.nasdroid.apps.logic.discover

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.app.AppV2Api

/**
 * Get a list of all available categories provided by all catalogs. See [invoke] for details.
 */
class GetAvailableCategories(
    private val appV2Api: AppV2Api
) {

    /**
     * Retrieves a list of Strings representing every available app category. Categories are
     * provided by catalogs, and as such have no guarantees on their human-friendliness.
     */
    suspend operator fun invoke(): Result<List<String>> {
        return try {
            val categories = appV2Api.getCategories()
            Result.success(categories)
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}