package com.boswelja.truemanager.core.api.v2.chart.release

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Describes the TrueNAS API V2 "chart.release" group. The Charts API is responsible for managing
 * installed applications.
 */
interface ChartReleaseV2Api {

    /**
     * Get a list of all chart releases available on the system.
     */
    suspend fun getChartReleases(): List<ChartRelease>

    /**
     * Create a new chart release on the system. This is equivalent to installing an application.
     *
     * @return An ID for a running job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun createChartRelease(newRelease: CreateChartRelease): Int

    /**
     * Deletes an existing chart release, including its data, from the system.
     *
     * @param id The ID of the chart to delete.
     * @return An ID for running a job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun deleteChartRelease(id: String): Int

    /**
     * Retrieves details of a single [ChartRelease] with the given ID.
     */
    suspend fun getChartRelease(id: String): ChartRelease

    /**
     * Updates the details for the chart release whose ID matches the given [ChartRelease.id].
     */
    suspend fun updateChartRelease(newChartData: ChartRelease)
}

/**
 * Describes a new chart release to be created.
 *
 * @property releaseName The user-specified name for the release.
 * @property catalogId The ID of the catalog item that will be configured for this chart.
 * @property train The catalog train the item comes from.
 * @property version The version of the catalog item to use.
 * @property values Per-catalog configuration values.
 */
@Serializable
data class CreateChartRelease(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("catalog")
    val catalogId: String,
    @SerialName("train")
    val train: String,
    @SerialName("version")
    val version: String,
    @SerialName("values")
    val values: Map<String, String>
)
