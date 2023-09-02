package com.nasdroid.api.v2.chart.release

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class ChartReleaseV2ApiImpl(
    private val httpClient: HttpClient
) : ChartReleaseV2Api {
    override suspend fun getChartReleases(): List<ChartRelease> {
        val response = httpClient.get("chart/release")
        return response.body()
    }

    override suspend fun createChartRelease(newRelease: CreateChartRelease): Int {
        val response = httpClient.post("chart/release") {
            setBody(newRelease)
        }
        return response.body()
    }

    override suspend fun deleteChartRelease(id: String): Int {
        val response = httpClient.delete("chart/release/id/$id")
        return response.body()
    }

    override suspend fun getChartRelease(id: String): ChartRelease {
        val response = httpClient.get("chart/release/id/$id")
        return response.body()
    }

    override suspend fun updateChartRelease(newChartData: ChartRelease) {
        httpClient.put("chart/release/id/${newChartData.id}") {
            setBody(newChartData)
        }
    }

    override suspend fun scale(releaseName: String, replicaCount: Int): Int {
        val response = httpClient.post("chart/release/scale") {
            setBody(ScaleReleaseBody(releaseName, ScaleReleaseBody.ScaleOptions(replicaCount)))
        }
        return response.body()
    }

    override suspend fun rollbackRelease(releaseName: String, options: RollbackOptions): Int {
        val response = httpClient.post("chart/release/rollback") {
            setBody(RollbackReleaseBody(releaseName, options))
        }
        return response.body()
    }

    override suspend fun getPodLogChoices(releaseName: String): PodLogChoices {
        val response = httpClient.post("chart/release/pod_logs_choices") {
            setBody("\"$releaseName\"")
        }
        return response.body()
    }

    override suspend fun getPodLogs(
        releaseName: String,
        podLogsOptions: PodLogsOptions
    ): List<String> {
        val response = httpClient.post("chart/release/pod_logs") {
            setBody(PodLogsBody(releaseName, podLogsOptions))
        }
        return response.bodyAsText().split("\n")
    }

    override suspend fun deleteRelease(id: String, deleteUnusedImages: Boolean): Int {
        val response = httpClient.delete("chart/release/id/$id") {
            setBody(DeleteAppBody(deleteUnusedImages))
        }
        return response.body()
    }

    override suspend fun getUpgradeSummary(
        releaseName: String,
        targetVersion: String?
    ): UpgradeSummary {
        val response = httpClient.post("chart/release/upgrade_summary") {
            if (targetVersion != null) {
                setBody(
                    UpgradeSummaryBody(
                        releaseName = releaseName,
                        options = UpgradeSummaryBody.Options(
                            itemVersion = targetVersion
                        )
                    )
                )
            } else {
                setBody(UpgradeSummaryBody(releaseName = releaseName))
            }
        }
        return response.body()
    }

    override suspend fun upgrade(releaseName: String, targetVersion: String): Int {
        val response = httpClient.post("chart/release/upgrade") {
            setBody(
                UpgradeBody(
                    releaseName = releaseName,
                    options = UpgradeBody.Options(
                        itemVersion = targetVersion
                    )
                )
            )
        }
        return response.body()
    }
}

@Serializable
internal data class ScaleReleaseBody(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("scale_options")
    val scaleOptions: ScaleOptions
) {
    @Serializable
    internal data class ScaleOptions(
        @SerialName("replica_count")
        val replicaCount: Int
    )
}

@Serializable
internal data class RollbackReleaseBody(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("rollback_options")
    val rollbackOptions: RollbackOptions,
)

@Serializable
internal data class PodLogsBody(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("options")
    val options: PodLogsOptions,
)

@Serializable
internal data class DeleteAppBody(
    @SerialName("delete_unused_images")
    val deleteUnusedImages: Boolean,
)

@Serializable
internal data class UpgradeSummaryBody(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("options")
    val options: Options = Options()
) {
    @Serializable
    data class Options(
        @SerialName("item_version")
        val itemVersion: String = "latest"
    )
}

@Serializable
internal data class UpgradeBody(
    @SerialName("release_name")
    val releaseName: String,
    @SerialName("upgrade_options")
    val options: Options = Options()
) {
    @Serializable
    data class Options(
        @SerialName("item_version")
        val itemVersion: String = "latest"
    )
}
