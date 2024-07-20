package com.nasdroid.apps.logic.discover

import com.nasdroid.api.exception.ClientRequestException
import com.nasdroid.api.v2.catalog.CatalogV2Api
import com.nasdroid.core.strongresult.StrongResult
import kotlinx.datetime.Instant

/**
 * Retrieves details about a single application that is available to be installed on the system. See
 * [invoke] for details.
 */
class GetAvailableAppDetails(
    private val catalogV2Api: CatalogV2Api,
    private val stripHtmlTags: StripHtmlTags
) {

    /**
     * Retrieves details for a specified available app, or a [AvailableAppDetailsError] if something
     * went wrong.
     *
     * @param id The unique identifier of the app whose details should be retrieved.
     * @param catalog The catalog that the app belongs to.
     * @param train The train within the catalog that the app is available on.
     */
    suspend operator fun invoke(
        id: String,
        catalog: String,
        train: String
    ): StrongResult<AvailableAppDetails, AvailableAppDetailsError> {
        val catalogDetails = try {
            catalogV2Api.getCatalogItemDetails(id, catalog, train)
        } catch (_: ClientRequestException) {
            // The API returns error 422 when it can't find an app, but also 422 for missing data.
            // Let's assume we always send the data it needs.
            return StrongResult.failure(AvailableAppDetailsError.AppNotFound)
        }
        val details = AvailableAppDetails(
            id = catalogDetails.name,
            iconUrl = catalogDetails.iconUrl.orEmpty(),
            name = catalogDetails.title,
            version = catalogDetails.latestAppVersion,
            tags = catalogDetails.tags,
            homepage = catalogDetails.homeUrl,
            description = stripHtmlTags(catalogDetails.appReadme),
            screenshots = catalogDetails.screenshotUrls,
            sources = catalogDetails.sourceUrls,
            lastUpdatedAt = Instant.fromEpochMilliseconds(catalogDetails.lastUpdate),
            chartDetails = AvailableAppDetails.ChartDetails(
                catalog = catalog,
                train = train,
                chartVersion = catalogDetails.latestVersion,
                maintainers = catalogDetails.maintainers.map {
                    AvailableAppDetails.ChartDetails.Maintainer(
                        name = it.name,
                        url = it.url,
                        email = it.email
                    )
                }
            )
        )

        return StrongResult.success(details)
    }
}

/**
 * Details about a single available application.
 *
 * @property id A unique identifier for this app within its catalog and train. This is usually
 * similar to the app name.
 * @property iconUrl The URL for the app icon.
 * @property name The name of the app.
 * @property version The version of the app that these details are for. This is usually also the
 * latest version of the app.
 * @property tags A list of tags that this app is associated with. For example,
 * `["media", "entertainment"]`.
 * @property homepage A URL linking to the apps homepage.
 * @property description A human-readable description of the app.
 * @property screenshots A list of URLs that point to screenshots of the app in action.
 * @property sources A list of valid source URLs that can be used to find out more about the app.
 * @property lastUpdatedAt Defines exactly when the app was last updated.
 * @property chartDetails Describes the underlying chart. See [ChartDetails] for more information.
 */
data class AvailableAppDetails(
    val id: String,
    val iconUrl: String,
    val name: String,
    val version: String,
    val tags: List<String>,
    val homepage: String?,
    val description: String,
    val screenshots: List<String>,
    val sources: List<String>,
    val lastUpdatedAt: Instant,
    val chartDetails: ChartDetails,
) {
    /**
     * Details about a Helm Chart underlying an app.
     *
     * @property catalog The catalog ID of the chart. All charts must belong to a catalog.
     * @property train The train within a catalog at which the app can be found. All charts are
     * served from trains within catalogs.
     * @property chartVersion The current version of the chart underlying the app.
     * @property maintainers A list of current maintainers and contact information.
     */
    data class ChartDetails(
        val catalog: String,
        val train: String,
        val chartVersion: String,
        val maintainers: List<Maintainer>,
    ) {

        /**
         * Details about a maintainer for a chart.
         *
         * @property name The maintainer name.
         * @property url The maintainers homepage.
         * @property email The maintainers email address.
         */
        data class Maintainer(
            val name: String,
            val url: String,
            val email: String,
        )
    }
}

/**
 * Encapsulates all possible failure modes for [GetAvailableAppDetails].
 */
sealed interface AvailableAppDetailsError {

    /**
     * Indicates that an app matching the given inputs was not found.
     */
    data object AppNotFound : AvailableAppDetailsError
}
