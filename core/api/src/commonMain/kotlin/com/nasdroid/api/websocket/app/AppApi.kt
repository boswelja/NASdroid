package com.nasdroid.api.websocket.app

import com.nasdroid.api.websocket.ddp.EDateInstantSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface AppApi {

    /**
     * Retrieve a list of all available apps from all catalogs.
     */
    suspend fun available(): List<AvailableApp>

    /**
     * Returns space available in bytes in the configured apps pool which apps can consume.
     */
    suspend fun availableSpace(): Int

    /**
     * Retrieve a list of categories for all available apps. Note catalogs define their own
     * categories.
     */
    suspend fun categories(): List<String>

    /**
     * Returns certificate authorities which can be used by applications.
     */
    suspend fun certificateAuthorityChoices(): List<CertificateAuthorityChoice>

    /**
     * Returns certificates which can be used by applications.
     */
    suspend fun certificateChoices(): List<CertificateAuthorityChoice>

    /**
     * Retrieve user specified configuration of [appName].
     */
    suspend fun config(appName: String): Map<String, String>

    /**
     * Returns container console choices for [appName].
     */
    suspend fun containerConsoleChoices(appName: String): Map<String, ContainerChoice>

    /**
     * Returns container IDs for [appName].
     */
    suspend fun containerIds(appName: String, aliveOnly: Boolean = true): Map<String, ContainerChoice>

    /**
     * Convert [appName] to a custom app.
     */
    suspend fun convertToCustom(appName: String): Int

    // TODO create

    /**
     * Delete [appName] app.
     *
     * [forceRemoveIxVolumes] should be set when the ix-volumes were created by the system for apps
     * which were migrated from k8s to docker and the user wants to remove them. This is to prevent
     * accidental deletion of the original ix-volumes which were created in dragonfish and before
     * for kubernetes based apps. When this is set, it will result in the deletion of ix-volumes
     * from both docker based apps and k8s based apps and should be carefully set.
     */
    suspend fun delete(
        appName: String,
        removeImages: Boolean = true,
        removeIxVolumes: Boolean = false,
        forceRemoveIxVolumes: Boolean = false
    ): Int

    // TODO get instance

    /**
     * Returns GPU choices which can be used by applications.
     */
    suspend fun gpuChoices(): List<String>

    /**
     * Returns IP choices which can be used by applications.
     */
    suspend fun ipChoices(): List<String>

    /**
     * Retrieve a list of available apps that have been recently updated.
     */
    suspend fun latest(): List<AvailableApp>

    /**
     * Returns a list of outdated docker images for the specified [appName].
     */
    suspend fun outdatedDockerImages(appName: String): List<String>

    /**
     * Pulls docker images for the specified [appName].
     */
    suspend fun pullImages(appName: String, redeploy: Boolean = true): Int

    // TODO query

    /**
     * Redeploy [appName] app.
     */
    suspend fun redeploy(appName: String): Int

    // TODO rollback

    /**
     * Retrieve versions available for rollback for [appName] app.
     */
    suspend fun rollbackVersions(appName: String): List<String>

    /**
     * Retrieve a list of apps that are similar to the specified app.
     *
     * @param appName The name of the app whose related apps should be retrieved.
     * @param train The catalog train which the app belongs to.
     */
    suspend fun similar(appName: String, train: String): List<AvailableApp>

    /**
     * Start [appName] app.
     */
    suspend fun start(appName: String): Int

    /**
     * Stop [appName] app.
     */
    suspend fun stop(appName: String): Int

    // TODO update

    // TODO upgrade

    // TODO upgrade summary

    /**
     * Returns ports in use by applications.
     */
    suspend fun usedPorts(): List<Int>
}

@Serializable
data class ContainerChoice(
    @SerialName("service_name")
    val serviceName: String,
    @SerialName("image")
    val image: String,
    @SerialName("state")
    val state: String,
    @SerialName("id")
    val id: String
)

@Serializable
data class CertificateAuthorityChoice(
    @SerialName("id")
    val id: Int?,
    @SerialName("type")
    val type: Int?,
    @SerialName("name")
    val name: String?,
    @SerialName("certificate")
    val certificate: String?,
    @SerialName("privatekey")
    val provateKey: String?,
    @SerialName("CSR")
    val csr: String?,
    @SerialName("acme_url")
    val acmeUrl: String?,
    @SerialName("domains_authenticators")
    val domainsAuthenticators: List<String>?,
    @SerialName("renew_days")
    val renewDays: Int,
    @SerialName("revokedDate")
    @Serializable(EDateInstantSerializer::class)
    val revokedDate: Instant,
    @SerialName("signedby")
    val signedBy: String?, // TODO unknown object
    @SerialName("root_path")
    val rootPath: String?,
    @SerialName("acme")
    val acme: String?, // TODO unknown object
    @SerialName("certificate_path")
    val certificatePath: String?,
    @SerialName("privatekey_path")
    val privateKeyPath: String?,
    @SerialName("csr_path")
    val csrPath: String?,
    @SerialName("cert_type")
    val certType: String?,
    @SerialName("revoked")
    val revoked: Boolean?,
    @SerialName("expired")
    val expired: Boolean?,
    @SerialName("issuer")
    val issuer: String?,
    @SerialName("chain_list")
    val chainList: List<String>?,
    @SerialName("country")
    val country: String?,
    @SerialName("state")
    val state: String?,
    @SerialName("city")
    val city: String?,
    @SerialName("organization")
    val organization: String?,
    @SerialName("organizational_unit")
    val organizationalUnit: String?,
    @SerialName("san")
    val san: List<String>?,
    @SerialName("email")
    val email: String?,
    @SerialName("DN")
    val dn: String?,
    @SerialName("subject_name_hash")
    val subjectNameHash: String?,
    @SerialName("digest_algorithm")
    val digestAlgorithm: String?,
    @SerialName("from")
    val from: String?,
    @SerialName("common")
    val common: String?,
    @SerialName("until")
    val until: String?,
    @SerialName("fingerprint")
    val fingerprint: String?,
    @SerialName("key_type")
    val keyType: String?,
    @SerialName("internal")
    val internal: String?,
    @SerialName("lifetime")
    val lifetime: Int?,
    @SerialName("serial")
    val serial: Int?,
    @SerialName("key_length")
    val keyLength: Int?,
    @SerialName("add_to_trusted_store")
    val addToTrustedStore: Boolean?,
    @SerialName("chain")
    val chain: Boolean?,
    @SerialName("CA_type_existing")
    val caTypeExisting: Boolean?,
    @SerialName("CA_type_internal")
    val caTypeInternal: Boolean?,
    @SerialName("CA_type_intermediate")
    val caTypeIntermediate: Boolean?,
    @SerialName("cert_type_existing")
    val certTypeExisting: Boolean?,
    @SerialName("cert_type_internal")
    val certTypeInternal: Boolean?,
    @SerialName("cert_type_CSR")
    val certTypeCsr: Boolean?,
    @SerialName("parsed")
    val parsed: Boolean?,
    @SerialName("can_be_revoked")
    val canBeRevoked: Boolean?,
    @SerialName("extensions")
    val extensions: String?, // TODO unknown object
    @SerialName("revoked_certs")
    val revokedCerts: List<String>?,
    @SerialName("crl_path")
    val crlPath: String?,
    @SerialName("signed_certificates")
    val signedCertificates: Int?

)
/**
 * Describes an application available to be installed on the system.
 *
 * @property catalog The catalog this app belongs to.
 * @property installed Whether an instance of this app is already installed.
 * @property train The catalog train this app belongs to.
 * @property htmlReadme The app README, using HTML formatting.
 * @property categories A list of categories this app belongs to.
 * @property description A brief overview of what the app is.
 * @property healthy Whether the app is considered "healthy".
 * @property healthyError When not [healthy], this contains the reason.
 * @property homeUrl A URL for the homepage of the app.
 * @property location The location on the system where the app information is stored.
 * @property latestVersion The latest version of the container the app runs in.
 * @property latestAppVersion The latest version of the software that runs inside the container.
 * @property latestHumanVersion A human-readable version that incorporates both [latestVersion] and
 * [latestAppVersion].
 * @property lastUpdate The timestamp in milliseconds since epoch of the last app update.
 * @property name The name of the app.
 * @property recommended Whether the system recommends this app to the user.
 * @property title The human-readable title of the app.
 * @property maintainers A list of active [maintainers].
 * @property tags A list of tags this app has.
 * @property screenshots A list of URLs for screenshots of this app. This may be empty.
 * @property sources A list of URLs pointing to the app source code.
 * @property iconUrl A URL for the app icon.
 */
@Serializable
data class AvailableApp(
    @SerialName("healthy")
    val healthy: Boolean,
    @SerialName("installed")
    val installed: Boolean,
    @SerialName("recommended")
    val recommended: Boolean,
    @SerialName("last_update")
    @Serializable(EDateInstantSerializer::class)
    val lastUpdate: Instant,
    @SerialName("capabilities")
    val capabilities: List<String>,
    @SerialName("run_as_context")
    val runAsContext: List<String>,
    @SerialName("categories")
    val categories: List<String>,
    @SerialName("maintainers")
    val maintainers: List<Maintainer>,
    @SerialName("tags")
    val tags: List<String>,
    @SerialName("screenshots")
    val screenshots: List<String>,
    @SerialName("sources")
    val sources: List<String>,
    @SerialName("name")
    val name: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("app_readme")
    val appReadme: String,
    @SerialName("location")
    val location: String,
    @SerialName("healthy_error")
    val healthyError: String?,
    @SerialName("home")
    val home: String,
    @SerialName("latest_version")
    val latestVersion: String,
    @SerialName("latest_app_version")
    val latestAppVersion: String,
    @SerialName("latest_human_version")
    val latestHumanVersion: String,
    @SerialName("icon_url")
    val iconUrl: String?,
    @SerialName("catalog")
    val catalog: String,
    @SerialName("train")
    val train: String,
) {

    /**
     * Describes a maintainer for an available app.
     *
     * @property email The maintainers email address.
     * @property name The name of the maintainer.
     * @property url A URL for the maintainers preferred homepage.
     */
    @Serializable
    data class Maintainer(
        @SerialName("email")
        val email: String,
        @SerialName("name")
        val name: String,
        @SerialName("url")
        val url: String,
    )
}
