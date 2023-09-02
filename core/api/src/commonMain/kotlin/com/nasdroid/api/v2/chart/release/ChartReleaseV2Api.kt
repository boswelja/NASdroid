package com.nasdroid.api.v2.chart.release

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

    /**
     * Gets the available log choices for the given release.
     */
    suspend fun getPodLogChoices(releaseName: String): PodLogChoices

    /**
     * Exports logs for a container in a pod for the given release.
     */
    suspend fun getPodLogs(releaseName: String, podLogsOptions: PodLogsOptions): List<String>

    /**
     * Deletes the release with the given ID.
     *
     * @return An ID for running a job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun deleteRelease(id: String, deleteUnusedImages: Boolean): Int

    /**
     * Gets an [UpgradeSummary] for an intended upgrade.
     */
    suspend fun getUpgradeSummary(releaseName: String, targetVersion: String? = null): UpgradeSummary

    /**
     * Upgrades a release to the specified version.
     *
     * @return An ID for running a job. See [com.boswelja.truemanager.core.api.v2.core.CoreV2Api.getJob].
     */
    suspend fun upgrade(releaseName: String, targetVersion: String): Int
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

/**
 * A Map of pod names to a list of container names.
 */
typealias PodLogChoices = Map<String, List<String>>

/**
 * Options for retrieving pod logs.
 *
 * @property podName The name of the pod hosting the desired container.
 * @property containerName The name of the container inside the target pod.
 * @property limitBytes The maximum number of bytes worth of logs to export.
 * @property tailLines The maximum number of log lines to retrieve.
 */
@Serializable
data class PodLogsOptions(
    @SerialName("pod_name")
    val podName: String,
    @SerialName("container_name")
    val containerName: String,
    @SerialName("limit_bytes")
    val limitBytes: Long? = null,
    @SerialName("tail_lines")
    val tailLines: Long = 500,
)

/**
 * Describes what will happen for an intended upgrade.
 *
 * @property containerImagesToUpdate A list of container images that will be upgraded.
 * @property changelog The item changelog, if available.
 * @property availableVersions A list of [UpgradeVersion]s that are available to upgrade to.
 * @property itemUpdateAvailable Whether the item will be updated with this upgrade.
 * @property imageUpdateAvailable Whether the image will be updated with this upgrade.
 * @property latestVersion The latest available version of the release.
 * @property upgradeVersion The target version that this summary describes.
 * @property latestHumanVersion A human-readable variant of [latestVersion].
 * @property upgradeHumanVersion A human-readable variant of [upgradeVersion].
 */
@Serializable
data class UpgradeSummary(
    @SerialName("container_images_to_update")
    val containerImagesToUpdate: List<String>,
    @SerialName("changelog")
    val changelog: String?,
    @SerialName("available_versions_for_upgrade")
    val availableVersions: List<UpgradeVersion>,
    @SerialName("item_update_available")
    val itemUpdateAvailable: Boolean,
    @SerialName("image_update_available")
    val imageUpdateAvailable: Boolean,
    @SerialName("latest_version")
    val latestVersion: String,
    @SerialName("upgrade_version")
    val upgradeVersion: String,
    @SerialName("latest_human_version")
    val latestHumanVersion: String,
    @SerialName("upgrade_human_version")
    val upgradeHumanVersion: String,
) {
    /**
     * Describes a version that can be upgraded to.
     *
     * @property version The version that the release can be upgraded to.
     * @property humanVersion A human-readable version of [version].
     */
    @Serializable
    data class UpgradeVersion(
        @SerialName("version")
        val version: String,
        @SerialName("human_version")
        val humanVersion: String,
    )
}
