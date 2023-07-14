package com.boswelja.truemanager.apps.logic.available

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api
import com.boswelja.truemanager.core.api.v2.chart.release.CreateChartRelease

/**
 * Installs a new application from a catalog item. See [invoke] for details.
 */
class InstallApplication(
    private val chartReleaseV2Api: ChartReleaseV2Api,
) {

    /**
     * Installs a new application based on the information in [NewApplication].
     */
    suspend operator fun invoke(newApplication: NewApplication) {
        chartReleaseV2Api.createChartRelease(
            CreateChartRelease(
                item = newApplication.name,
                releaseName = newApplication.title,
                catalogId = newApplication.catalog,
                train = newApplication.train,
                version = newApplication.version,
                values = newApplication.values
            )
        )
    }
}

/**
 * Describes an application to be installed from an existing catalog.
 *
 * @property name The name of the catalog item, as specified from the server.
 * @property title The user-specified name for the release.
 * @property catalog The ID of the catalog item that will be configured for this chart.
 * @property train The catalog train the item comes from.
 * @property version The version of the catalog item to use.
 * @property values Per-catalog configuration values.
 */
data class NewApplication(
    val name: String,
    val title: String,
    val catalog: String,
    val train: String,
    val version: String,
    val values: Map<String, String>
)
