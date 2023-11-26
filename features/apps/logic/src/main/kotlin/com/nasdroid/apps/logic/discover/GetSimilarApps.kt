package com.nasdroid.apps.logic.discover

import com.nasdroid.api.v2.app.AppV2Api
import com.nasdroid.apps.logic.discover.AvailableApp.Companion.toSanitizedModel

/**
 * Retrieves a list of [AvailableApp]s similar to another specified app. See [invoke] for details.
 */
class GetSimilarApps(
    private val appV2Api: AppV2Api
) {
    /**
     * Retrieves a list of [AvailableApp]s similar to the app specified by parameters.
     *
     * @param appId The [AvailableApp.id] of the app whose similar apps we are looking for.
     * @param catalogName The [AvailableApp.catalogName] of the app whose similar apps we are
     * looking for.
     * @param catalogTrain The [AvailableApp.catalogTrain] of the app whose similar apps we are
     * looking for.
     */
    suspend operator fun invoke(appId: String, catalogName: String, catalogTrain: String): Result<List<AvailableApp>> {
        val result = appV2Api.getSimilarTo(appId, catalogName, catalogTrain)
            .map { it.toSanitizedModel() }
        return Result.success(result)
    }
}
