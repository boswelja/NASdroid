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

    /**
     * Scales [releaseName] to [replicaCount] instances.
     *
     * @return An ID for running a job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun scale(releaseName: String, replicaCount: Int): Int

    /**
     * Roll back a release to a specified version.
     *
     * @return An ID for running a job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun rollbackRelease(releaseName: String, options: RollbackOptions): Int
}

/**
 * Describes a new chart release to be created.
 *
 * @property item The name of the item in the catalog.
 * @property releaseName The user-specified name for the release.
 * @property catalogId The ID of the catalog item that will be configured for this chart.
 * @property train The catalog train the item comes from.
 * @property version The version of the catalog item to use.
 * @property values Per-catalog configuration values.
 */
@Serializable
data class CreateChartRelease(
    @SerialName("item")
    val item: String,
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

/**
 * Options for rolling back a release. See [ChartReleaseV2Api.rollbackRelease].
 *
 * @property itemVersion The version to roll the chart release back to.
 * @property forceRollback Force the rollback operation to proceed even when no snapshots are found.
 * This is only considered when [rollbackSnapshot] is set.
 * @property recreateResources If true, the release Kubernetes resources will be deleted and
 * recreated.
 * @property rollbackSnapshot If true, roll back any PVC or IX Volume snapshots consumed by the
 * chart.
 */
@Serializable
data class RollbackOptions(
    @SerialName("item_version")
    val itemVersion: String,
    @SerialName("force_rollback")
    val forceRollback: Boolean = false,
    @SerialName("recreate_resources")
    val recreateResources: Boolean = false,
    @SerialName("rollback_snapshot")
    val rollbackSnapshot: Boolean = true,
)