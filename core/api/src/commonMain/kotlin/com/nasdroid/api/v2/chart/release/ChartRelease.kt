@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicProperty") // Yeah, no.
package com.nasdroid.api.v2.chart.release

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class ChartRelease(
    @SerialName("name")
    val name: String,
    @SerialName("info")
    val info: Info,
    @SerialName("config")
    val config: Map<String, JsonElement>,
    @SerialName("version")
    val version: Int,
    @SerialName("namespace")
    val namespace: String,
    @SerialName("chart_metadata")
    val chartMetadata: ChartMetadata,
    @SerialName("id")
    val id: String,
    @SerialName("catalog")
    val catalog: String,
    @SerialName("catalog_train")
    val catalogTrain: String,
    @SerialName("path")
    val path: String,
    @SerialName("dataset")
    val dataset: String,
    @SerialName("status")
    val status: Status,
    @SerialName("used_ports")
    val usedPorts: List<UsedPort>,
    @SerialName("pod_status")
    val podStatus: PodStatus,
    @SerialName("update_available")
    val updateAvailable: Boolean,
    @SerialName("human_version")
    val humanVersion: String,
    @SerialName("human_latest_version")
    val humanLatestVersion: String,
    @SerialName("container_images_update_available")
    val containerImagesUpdateAvailable: Boolean,
    @SerialName("portals")
    val portals: Portals?,
    @SerialName("hooks")
    val hooks: List<Hook>?,
    @SerialName("history")
    val history: Map<String, ChartHistoryItem>?
) {
    @Serializable
    enum class Status {
        @SerialName("DEPLOYING")
        DEPLOYING,
        @SerialName("ACTIVE")
        ACTIVE,
        @SerialName("STOPPED")
        STOPPED
    }

    @Serializable
    data class Info(
        @SerialName("first_deployed")
        val firstDeployed: String,
        @SerialName("last_deployed")
        val lastDeployed: String,
        @SerialName("deleted")
        val deleted: String,
        @SerialName("description")
        val description: String,
        @SerialName("status")
        val status: String,
        @SerialName("notes")
        val notes: String?
    )

    @Serializable
    data class ChartMetadata(
        @SerialName("name")
        val name: String,
        @SerialName("home")
        val home: String,
        @SerialName("version")
        val version: String,
        @SerialName("description")
        val description: String,
        @SerialName("maintainers")
        val maintainers: List<Maintainer>,
        @SerialName("icon")
        val icon: String,
        @SerialName("apiVersion")
        val apiVersion: String,
        @SerialName("appVersion")
        val appVersion: String,
        @SerialName("annotations")
        val annotations: Map<String, String>,
        @SerialName("kubeVersion")
        val kubeVersion: String,
        @SerialName("dependencies")
        val dependencies: List<Dependency>,
        @SerialName("type")
        val type: String?,
        @SerialName("latest_chart_version")
        val latestChartVersion: String,
        @SerialName("sources")
        val sources: List<String>?,
        @SerialName("keywords")
        val keywords: List<String>?,
    ) {
        @Serializable
        data class Maintainer(
            @SerialName("name")
            val name: String,
            @SerialName("email")
            val email: String,
            @SerialName("url")
            val url: String
        )

        @Serializable
        data class Dependency(
            @SerialName("name")
            val name: String,
            @SerialName("version")
            val version: String,
            @SerialName("repository")
            val repository: String,
            @SerialName("enabled")
            val enabled: Boolean
        )
    }

    @Serializable
    data class UsedPort(
        @SerialName("port")
        val port: Int,
        @SerialName("protocol")
        val protocol: String
    )

    @Serializable
    data class PodStatus(
        @SerialName("desired")
        val desired: Int,
        @SerialName("available")
        val available: Int
    )

    @Serializable(with = PortalsSerializer::class)
    sealed class Portals {
        abstract val webPortal: List<String>?
        abstract val open: List<String>?

        @Serializable
        data class WebPortal(
            @SerialName("web_portal")
            override val webPortal: List<String>?,
            @SerialName("open")
            override val open: List<String>?
        ) : Portals()

        @Serializable
        data class BadWebPortal(
            @SerialName("Web Portal")
            override val webPortal: List<String>?,
            @SerialName("open")
            override val open: List<String>?
        ) : Portals()
    }

    @Serializable
    data class Hook(
        @SerialName("name")
        val name: String,
        @SerialName("kind")
        val kind: String,
        @SerialName("path")
        val path: String,
        @SerialName("manifest")
        val manifest: String,
        @SerialName("events")
        val events: List<String>,
        @SerialName("last_run")
        val lastRun: LastRun,
        @SerialName("delete_policies")
        val deletePolicies: List<String>,
        @SerialName("weight")
        val weight: Int?
    ) {
        @Serializable
        data class LastRun(
            @SerialName("started_at")
            val startedAt: String,
            @SerialName("completed_at")
            val completedAt: String,
            @SerialName("phase")
            val phase: String
        )
    }
}
@Serializable
data class ChartHistoryItem(
    @SerialName("name")
    val name: String,
    @SerialName("info")
    val info: Info,
    @SerialName("config")
    val config: Map<String, JsonElement>,
    @SerialName("version")
    val version: Int,
    @SerialName("namespace")
    val namespace: String,
    @SerialName("chart_metadata")
    val chartMetadata: ChartMetadata,
    @SerialName("id")
    val id: String,
    @SerialName("catalog")
    val catalog: String,
    @SerialName("catalog_train")
    val catalogTrain: String,
    @SerialName("human_version")
    val humanVersion: String,
) {
    @Serializable
    data class Info(
        @SerialName("first_deployed")
        val firstDeployed: String,
        @SerialName("last_deployed")
        val lastDeployed: String,
        @SerialName("deleted")
        val deleted: String,
        @SerialName("description")
        val description: String,
        @SerialName("status")
        val status: String,
        @SerialName("notes")
        val notes: String?
    )

    @Serializable
    data class ChartMetadata(
        @SerialName("name")
        val name: String,
        @SerialName("home")
        val home: String,
        @SerialName("version")
        val version: String,
        @SerialName("description")
        val description: String,
        @SerialName("maintainers")
        val maintainers: List<Maintainer>,
        @SerialName("icon")
        val icon: String,
        @SerialName("apiVersion")
        val apiVersion: String,
        @SerialName("appVersion")
        val appVersion: String,
        @SerialName("annotations")
        val annotations: Map<String, String>,
        @SerialName("kubeVersion")
        val kubeVersion: String,
        @SerialName("dependencies")
        val dependencies: List<Dependency>,
        @SerialName("type")
        val type: String?,
        @SerialName("sources")
        val sources: List<String>?,
        @SerialName("keywords")
        val keywords: List<String>?,
    ) {
        @Serializable
        data class Maintainer(
            @SerialName("name")
            val name: String,
            @SerialName("email")
            val email: String,
            @SerialName("url")
            val url: String
        )

        @Serializable
        data class Dependency(
            @SerialName("name")
            val name: String,
            @SerialName("version")
            val version: String,
            @SerialName("repository")
            val repository: String,
            @SerialName("enabled")
            val enabled: Boolean
        )
    }
}

internal object PortalsSerializer :
    JsonContentPolymorphicSerializer<ChartRelease.Portals>(ChartRelease.Portals::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "Web Portal" in element.jsonObject -> ChartRelease.Portals.BadWebPortal.serializer()
        else -> ChartRelease.Portals.WebPortal.serializer()
    }
}
