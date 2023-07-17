@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicProperty") // Yeah, no.
package com.boswelja.truemanager.core.api.v2.chart.release

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SealedClassSerializer
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
    val config: Config,
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
    val status: String,
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
    val hooks: List<Hook>?
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
    data class Config(
        @SerialName("containerArgs")
        val containerArgs: List<String>?,
        @SerialName("containerCommand")
        val containerCommand: List<String>?,
        @SerialName("containerEnvironmentVariables")
        val containerEnvironmentVariables: List<String>?,
        @SerialName("dnsConfig")
        val dnsConfig: DnsConfig?,
        @SerialName("dnsPolicy")
        val dnsPolicy: String?,
        @SerialName("emptyDirVolumes")
        val emptyDirVolumes: List<String>?,
        @SerialName("enableResourceLimits")
        val enableResourceLimits: Boolean?,
        @SerialName("enableUIPortal")
        val enableUIPortal: Boolean?,
        @SerialName("externalInterfaces")
        val externalInterfaces: List<ExternalInterface>?,
        @SerialName("global")
        val global: Global,
        @SerialName("gpuConfiguration")
        val gpuConfiguration: GpuConfiguration?,
        @SerialName("hostPathVolumes")
        val hostPathVolumes: List<HostPathVolume>?,
        @SerialName("hostPortsList")
        val hostPortsList: List<String>?,
        @SerialName("image")
        val image: Image,
        @SerialName("ixCertificateAuthorities")
        val ixCertificateAuthorities: IxCertificateAuthorities?,
        @SerialName("ixCertificates")
        val ixCertificates: IxCertificates?,
        @SerialName("ixChartContext")
        val ixChartContext: IxChartContext,
        @SerialName("ixExternalInterfacesConfiguration")
        val ixExternalInterfacesConfiguration: List<String>,
        @SerialName("ixExternalInterfacesConfigurationNames")
        val ixExternalInterfacesConfigurationNames: List<String>,
        @SerialName("ixVolumes")
        val ixVolumes: List<IxVolume>,
        @SerialName("livenessProbe")
        val livenessProbe: String?,
        @SerialName("placeholder")
        val placeholder: Placeholder?,
        @SerialName("portForwardingList")
        val portForwardingList: List<PortForwarding>?,
        @SerialName("portalDetails")
        val portalDetails: PortalDetails?,
        @SerialName("release_name")
        val releaseName: String,
        @SerialName("securityContext")
        val securityContext: SecurityContext?,
        @SerialName("stdin")
        val stdin: Boolean?,
        @SerialName("tty")
        val tty: Boolean?,
        @SerialName("updateStrategy")
        val updateStrategy: String?,
        @SerialName("volumes")
        val volumes: List<String>?,
        @SerialName("workloadType")
        val workloadType: String?,
        @SerialName("hostNetwork")
        val hostNetwork: Boolean?,
        @SerialName("appVolumeMounts")
        val appVolumeMounts: AppVolumeMounts?,
        @SerialName("environmentVariables")
        val environmentVariables: List<String>?,
        @SerialName("extraAppVolumeMounts")
        val extraAppVolumeMounts: List<ExtraAppVolumeMount>?,
        @SerialName("ownerGID")
        val ownerGID: Int?,
        @SerialName("ownerUID")
        val ownerUID: Int?,
        @SerialName("tcp_port")
        val tcpPort: Int?,
        @SerialName("udp_port")
        val udpPort: Int?,
        @SerialName("web_port")
        val webPort: Int?,
        @SerialName("resources")
        val resources: Resources?,
        @SerialName("sonarrConfig")
        val sonarrConfig: SonarrConfig?,
        @SerialName("sonarrNetwork")
        val sonarrNetwork: SonarrNetwork?,
        @SerialName("sonarrRunAs")
        val sonarrRunAs: SonarrRunAs?,
        @SerialName("sonarrStorage")
        val sonarrStorage: SonarrStorage?,
        @SerialName("notes")
        val notes: Notes?,
        @SerialName("qbitConfig")
        val qbitConfig: QbitConfig?,
        @SerialName("qbitNetwork")
        val qbitNetwork: QbitNetwork?,
        @SerialName("qbitRunAs")
        val qbitRunAs: QbitRunAs?,
        @SerialName("qbitStorage")
        val qbitStorage: QbitStorage?,
        @SerialName("TZ")
        val tZ: String?,
        @SerialName("addons")
        val addons: Addons?,
        @SerialName("auth")
        val auth: Auth?,
        @SerialName("configmap")
        val configmap: Configmap?,
        @SerialName("deviceList")
        val deviceList: List<String>?,
        @SerialName("docs")
        val docs: Docs?,
        @SerialName("donateNag")
        val donateNag: DonateNag?,
        @SerialName("ingress")
        val ingress: Ingress?,
        @SerialName("ingressList")
        val ingressList: List<String>?,
        @SerialName("networkPolicy")
        val networkPolicy: List<String>?,
        @SerialName("persistence")
        val persistence: PersistenceListItem?,
        @SerialName("persistenceList")
        val persistenceList: List<Persistence>?,
        @SerialName("podOptions")
        val podOptions: PodOptions?,
        @SerialName("portal")
        val portal: Portal?,
        @SerialName("scaleExternalInterface")
        val scaleExternalInterface: List<ScaleExternalInterface>?,
        @SerialName("scaleGPU")
        val scaleGPU: List<String>?,
        @SerialName("service")
        val service: Service?,
        @SerialName("serviceList")
        val serviceList: List<String>?,
        @SerialName("serviceexpert")
        val serviceexpert: Boolean?,
        @SerialName("websockets")
        val websockets: Websockets?,
        @SerialName("workload")
        val workload: Workload?,
        @SerialName("adguardNetwork")
        val adguardNetwork: AdguardNetwork?,
        @SerialName("adguardStorage")
        val adguardStorage: AdguardStorage?,
        @SerialName("autodiscovery")
        val autodiscovery: Autodiscovery?,
        @SerialName("broadcastProxyImage")
        val broadcastProxyImage: BroadcastProxyImage?
    ) {
        @Serializable
        data class DnsConfig(
            @SerialName("nameservers")
            val nameservers: List<String>?,
            @SerialName("options")
            val options: List<String>,
            @SerialName("searches")
            val searches: List<String>?
        )

        @Serializable
        data class ExternalInterface(
            @SerialName("hostInterface")
            val hostInterface: String,
            @SerialName("ipam")
            val ipam: Ipam
        ) {
            @Serializable
            data class Ipam(
                @SerialName("staticIPConfigurations")
                val staticIPConfigurations: List<String>,
                @SerialName("staticRoutes")
                val staticRoutes: List<String>,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Global(
            @SerialName("ixChartContext")
            val ixChartContext: IxChartContext,
            @SerialName("stopAll")
            val stopAll: Boolean?
        ) {
            @Serializable
            data class IxChartContext(
                @SerialName("isInstall")
                val isInstall: Boolean,
                @SerialName("isUpdate")
                val isUpdate: Boolean,
                @SerialName("isUpgrade")
                val isUpgrade: Boolean,
                @SerialName("kubernetes_config")
                val kubernetesConfig: KubernetesConfig,
                @SerialName("operation")
                val operation: String,
                @SerialName("storageClassName")
                val storageClassName: String,
                @SerialName("upgradeMetadata")
                val upgradeMetadata: UpgradeMetadata?
            ) {
                @Serializable
                data class KubernetesConfig(
                    @SerialName("cluster_cidr")
                    val clusterCidr: String,
                    @SerialName("cluster_dns_ip")
                    val clusterDnsIp: String,
                    @SerialName("service_cidr")
                    val serviceCidr: String
                )

                @Serializable
                data class UpgradeMetadata(
                    @SerialName("newChartVersion")
                    val newChartVersion: String?,
                    @SerialName("oldChartVersion")
                    val oldChartVersion: String?,
                    @SerialName("preUpgradeRevision")
                    val preUpgradeRevision: Int?
                )
            }
        }

        @Serializable
        data class GpuConfiguration(
            @SerialName("nvidia.com/gpu")
            val nvidiaComgpu: Int?
        )

        @Serializable
        data class HostPathVolume(
            @SerialName("hostPath")
            val hostPath: String,
            @SerialName("mountPath")
            val mountPath: String,
            @SerialName("readOnly")
            val readOnly: Boolean
        )

        @Serializable
        data class Image(
            @SerialName("pullPolicy")
            val pullPolicy: String,
            @SerialName("repository")
            val repository: String,
            @SerialName("tag")
            val tag: String
        )

        @Serializable
        class IxCertificateAuthorities

        @Serializable
        class IxCertificates

        @Serializable
        data class IxChartContext(
            @SerialName("isInstall")
            val isInstall: Boolean,
            @SerialName("isUpdate")
            val isUpdate: Boolean,
            @SerialName("isUpgrade")
            val isUpgrade: Boolean,
            @SerialName("kubernetes_config")
            val kubernetesConfig: KubernetesConfig,
            @SerialName("operation")
            val operation: String,
            @SerialName("storageClassName")
            val storageClassName: String,
            @SerialName("upgradeMetadata")
            val upgradeMetadata: UpgradeMetadata?
        ) {
            @Serializable
            data class KubernetesConfig(
                @SerialName("cluster_cidr")
                val clusterCidr: String,
                @SerialName("cluster_dns_ip")
                val clusterDnsIp: String,
                @SerialName("service_cidr")
                val serviceCidr: String
            )

            @Serializable
            data class UpgradeMetadata(
                @SerialName("newChartVersion")
                val newChartVersion: String?,
                @SerialName("oldChartVersion")
                val oldChartVersion: String?,
                @SerialName("preUpgradeRevision")
                val preUpgradeRevision: Int?
            )
        }

        @Serializable
        data class IxVolume(
            @SerialName("hostPath")
            val hostPath: String
        )

        @Serializable
        class Placeholder

        @Serializable
        data class PortForwarding(
            @SerialName("containerPort")
            val containerPort: Int,
            @SerialName("nodePort")
            val nodePort: Int,
            @SerialName("protocol")
            val protocol: String
        )

        @Serializable
        data class PortalDetails(
            @SerialName("host")
            val host: String,
            @SerialName("port")
            val port: Int,
            @SerialName("portalName")
            val portalName: String,
            @SerialName("protocol")
            val protocol: String,
            @SerialName("useNodeIP")
            val useNodeIP: Boolean
        )

        @Serializable
        data class SecurityContext(
            @SerialName("capabilities")
            val capabilities: List<String>?,
            @SerialName("enableRunAsUser")
            val enableRunAsUser: Boolean?,
            @SerialName("privileged")
            val privileged: Boolean?,
            @SerialName("container")
            val container: Container?,
            @SerialName("pod")
            val pod: Pod?
        ) {
            @Serializable
            data class Container(
                @SerialName("UMASK")
                val uMASK: String,
                @SerialName("advanced")
                val advanced: Boolean,
                @SerialName("runAsGroup")
                val runAsGroup: Int,
                @SerialName("runAsUser")
                val runAsUser: Int,
                @SerialName("readOnlyRootFilesystem")
                val readOnlyRootFilesystem: Boolean?
            )

            @Serializable
            data class Pod(
                @SerialName("fsGroup")
                val fsGroup: Int,
                @SerialName("fsGroupChangePolicy")
                val fsGroupChangePolicy: String,
                @SerialName("supplementalGroups")
                val supplementalGroups: List<String>
            )
        }

        @Serializable
        data class AppVolumeMounts(
            @SerialName("config")
            val config: Config
        ) {
            @Serializable
            data class Config(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("hostPathEnabled")
                val hostPathEnabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String
            )
        }

        @Serializable
        data class ExtraAppVolumeMount(
            @SerialName("hostPath")
            val hostPath: String,
            @SerialName("mountPath")
            val mountPath: String
        )

        @Serializable
        data class Resources(
            @SerialName("limits")
            val limits: Limits,
            @SerialName("requests")
            val requests: Requests?
        ) {
            @Serializable
            data class Limits(
                @SerialName("cpu")
                val cpu: String,
                @SerialName("memory")
                val memory: String
            )

            @Serializable
            data class Requests(
                @SerialName("cpu")
                val cpu: String,
                @SerialName("memory")
                val memory: String
            )
        }

        @Serializable
        data class SonarrConfig(
            @SerialName("additionalEnvs")
            val additionalEnvs: List<String>,
            @SerialName("instanceName")
            val instanceName: String
        )

        @Serializable
        data class SonarrNetwork(
            @SerialName("hostNetwork")
            val hostNetwork: Boolean,
            @SerialName("webPort")
            val webPort: Int
        )

        @Serializable
        data class SonarrRunAs(
            @SerialName("group")
            val group: Int,
            @SerialName("user")
            val user: Int
        )

        @Serializable
        data class SonarrStorage(
            @SerialName("additionalStorages")
            val additionalStorages: List<AdditionalStorage>,
            @SerialName("config")
            val config: Config
        ) {
            @Serializable
            data class AdditionalStorage(
                @SerialName("hostPath")
                val hostPath: String,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Config(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Notes(
            @SerialName("custom")
            val custom: String
        )

        @Serializable
        data class QbitConfig(
            @SerialName("additionalEnvs")
            val additionalEnvs: List<String>
        )

        @Serializable
        data class QbitNetwork(
            @SerialName("btPort")
            val btPort: Int,
            @SerialName("hostNetwork")
            val hostNetwork: Boolean,
            @SerialName("webPort")
            val webPort: Int
        )

        @Serializable
        data class QbitRunAs(
            @SerialName("group")
            val group: Int,
            @SerialName("user")
            val user: Int
        )

        @Serializable
        data class QbitStorage(
            @SerialName("config")
            val config: Config,
            @SerialName("downloads")
            val downloads: Downloads
        ) {
            @Serializable
            data class Config(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Downloads(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("hostPath")
                val hostPath: String,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Addons(
            @SerialName("codeserver")
            val codeserver: Codeserver,
            @SerialName("netshoot")
            val netshoot: Netshoot,
            @SerialName("vpn")
            val vpn: Vpn
        ) {
            @Serializable
            data class Codeserver(
                @SerialName("enabled")
                val enabled: Boolean
            )

            @Serializable
            data class Netshoot(
                @SerialName("enabled")
                val enabled: Boolean
            )

            @Serializable
            data class Vpn(
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Auth(
            @SerialName("enabled")
            val enabled: Boolean
        )

        @Serializable
        data class Configmap(
            @SerialName("config")
            val config: Config
        ) {
            @Serializable
            data class Config(
                @SerialName("data")
                val `data`: Data,
                @SerialName("enabled")
                val enabled: Boolean
            ) {
                @Serializable
                data class Data(
                    @SerialName("mosquitto.conf")
                    val mosquittoConf: String
                )
            }
        }

        @Serializable
        data class Docs(
            @SerialName("confirmDocs")
            val confirmDocs: Boolean
        )

        @Serializable
        data class DonateNag(
            @SerialName("confirmDonate")
            val confirmDonate: Boolean
        )

        @Serializable
        data class Ingress(
            @SerialName("main")
            val main: Main,
            @SerialName("websockets")
            val websockets: Websockets?
        ) {
            @Serializable
            data class Main(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("entrypoint")
                val entrypoint: String,
                @SerialName("ingressClassName")
                val ingressClassName: String,
                @SerialName("tls")
                val tls: List<String>
            )

            @Serializable
            data class Websockets(
                @SerialName("autoLink")
                val autoLink: Boolean,
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("entrypoint")
                val entrypoint: String,
                @SerialName("ingressClassName")
                val ingressClassName: String,
                @SerialName("tls")
                val tls: List<String>
            )
        }

        @Serializable
        data class PersistenceListItem(
            @SerialName("configinc")
            val configinc: Configinc?,
            @SerialName("data")
            val `data`: Data?,
            @SerialName("mosquitto-config")
            val mosquittoConfig: MosquittoConfig?,
            @SerialName("cache")
            val cache: Cache?,
            @SerialName("config")
            val config: Config?,
            @SerialName("transcode")
            val transcode: Transcode?
        ) {
            @Serializable
            data class Configinc(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("readOnly")
                val readOnly: Boolean,
                @SerialName("size")
                val size: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Data(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("readOnly")
                val readOnly: Boolean,
                @SerialName("size")
                val size: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class MosquittoConfig(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("objectName")
                val objectName: String,
                @SerialName("subPath")
                val subPath: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Cache(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Config(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("readOnly")
                val readOnly: Boolean,
                @SerialName("size")
                val size: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Transcode(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("mountPath")
                val mountPath: String,
                @SerialName("readOnly")
                val readOnly: Boolean,
                @SerialName("size")
                val size: String,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Persistence(
            @SerialName("enabled")
            val enabled: Boolean,
            @SerialName("hostPath")
            val hostPath: String,
            @SerialName("mountPath")
            val mountPath: String,
            @SerialName("readOnly")
            val readOnly: Boolean,
            @SerialName("setPermissions")
            val setPermissions: Boolean,
            @SerialName("type")
            val type: String,
            @SerialName("autoPermissions")
            val autoPermissions: AutoPermissions
        ) {
            @Serializable
            data class AutoPermissions(
                @SerialName("enabled")
                val enabled: Boolean
            )
        }

        @Serializable
        data class PodOptions(
            @SerialName("expertPodOpts")
            val expertPodOpts: Boolean
        )

        @Serializable
        data class Portal(
            @SerialName("open")
            val `open`: Open
        ) {
            @Serializable
            data class Open(
                @SerialName("enabled")
                val enabled: Boolean
            )
        }

        @Serializable
        data class ScaleExternalInterface(
            @SerialName("hostInterface")
            val hostInterface: String,
            @SerialName("ipam")
            val ipam: Ipam
        ) {
            @Serializable
            data class Ipam(
                @SerialName("staticIPConfigurations")
                val staticIPConfigurations: List<String>,
                @SerialName("staticRoutes")
                val staticRoutes: List<String>,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Service(
            @SerialName("main")
            val main: Main,
            @SerialName("websockets")
            val websockets: Websockets?,
            @SerialName("autodiscovery")
            val autodiscovery: Autodiscovery?
        ) {
            @Serializable
            data class Main(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("loadBalancerIP")
                val loadBalancerIP: String,
                @SerialName("ports")
                val ports: Ports,
                @SerialName("type")
                val type: String
            ) {
                @Serializable
                data class Ports(
                    @SerialName("main")
                    val main: Main
                ) {
                    @Serializable
                    data class Main(
                        @SerialName("port")
                        val port: Int,
                        @SerialName("targetPort")
                        val targetPort: Int
                    )
                }
            }

            @Serializable
            data class Websockets(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("ports")
                val ports: Ports,
                @SerialName("type")
                val type: String
            ) {
                @Serializable
                data class Ports(
                    @SerialName("websockets")
                    val websockets: Websockets
                ) {
                    @Serializable
                    data class Websockets(
                        @SerialName("enabled")
                        val enabled: Boolean,
                        @SerialName("port")
                        val port: Int,
                        @SerialName("targetPort")
                        val targetPort: Int
                    )
                }
            }

            @Serializable
            data class Autodiscovery(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("ports")
                val ports: Ports
            ) {
                @Serializable
                data class Ports(
                    @SerialName("autodiscovery")
                    val autodiscovery: Autodiscovery
                ) {
                    @Serializable
                    data class Autodiscovery(
                        @SerialName("enabled")
                        val enabled: Boolean,
                        @SerialName("port")
                        val port: Int,
                        @SerialName("protocol")
                        val protocol: String,
                        @SerialName("targetPort")
                        val targetPort: Int
                    )
                }
            }
        }

        @Serializable
        data class Websockets(
            @SerialName("enabled")
            val enabled: Boolean
        )

        @Serializable
        data class Workload(
            @SerialName("main")
            val main: Main,
            @SerialName("broadcastproxy")
            val broadcastproxy: Broadcastproxy?
        ) {
            @Serializable
            data class Main(
                @SerialName("podSpec")
                val podSpec: PodSpec,
                @SerialName("replicas")
                val replicas: Int,
                @SerialName("type")
                val type: String
            ) {
                @Serializable
                data class PodSpec(
                    @SerialName("containers")
                    val containers: Containers
                ) {
                    @Serializable
                    data class Containers(
                        @SerialName("main")
                        val main: Main
                    ) {
                        @Serializable
                        data class Main(
                            @SerialName("advanced")
                            val advanced: Boolean,
                            @SerialName("envList")
                            val envList: List<String>,
                            @SerialName("extraArgs")
                            val extraArgs: List<String>,
                            @SerialName("probes")
                            val probes: Probes?,
                            @SerialName("autodiscovery")
                            val autodiscovery: Autodiscovery?,
                            @SerialName("env")
                            val env: Env?
                        ) {
                            @Serializable
                            data class Probes(
                                @SerialName("liveness")
                                val liveness: Liveness,
                                @SerialName("readiness")
                                val readiness: Readiness,
                                @SerialName("startup")
                                val startup: Startup
                            ) {
                                @Serializable
                                data class Liveness(
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("port")
                                    val port: Int,
                                    @SerialName("type")
                                    val type: String
                                )

                                @Serializable
                                data class Readiness(
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("port")
                                    val port: Int,
                                    @SerialName("type")
                                    val type: String
                                )

                                @Serializable
                                data class Startup(
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("port")
                                    val port: Int,
                                    @SerialName("type")
                                    val type: String
                                )
                            }

                            @Serializable
                            data class Autodiscovery(
                                @SerialName("enabled")
                                val enabled: Boolean
                            )

                            @Serializable
                            data class Env(
                                @SerialName("JELLYFIN_PublishedServerUrl")
                                val jELLYFINPublishedServerUrl: String
                            )
                        }
                    }
                }
            }

            @Serializable
            data class Broadcastproxy(
                @SerialName("enabled")
                val enabled: Boolean,
                @SerialName("podSpec")
                val podSpec: PodSpec,
                @SerialName("type")
                val type: String
            ) {
                @Serializable
                data class PodSpec(
                    @SerialName("containers")
                    val containers: Containers,
                    @SerialName("hostNetwork")
                    val hostNetwork: Boolean,
                    @SerialName("terminationGracePeriodSeconds")
                    val terminationGracePeriodSeconds: Int
                ) {
                    @Serializable
                    data class Containers(
                        @SerialName("broadcastproxy")
                        val broadcastproxy: Broadcastproxy
                    ) {
                        @Serializable
                        data class Broadcastproxy(
                            @SerialName("args")
                            val args: List<String>,
                            @SerialName("command")
                            val command: List<String>,
                            @SerialName("enabled")
                            val enabled: Boolean,
                            @SerialName("imageSelector")
                            val imageSelector: String,
                            @SerialName("primary")
                            val primary: Boolean,
                            @SerialName("probes")
                            val probes: Probes,
                            @SerialName("securityContext")
                            val securityContext: SecurityContext
                        ) {
                            @Serializable
                            data class Probes(
                                @SerialName("liveness")
                                val liveness: Liveness,
                                @SerialName("readiness")
                                val readiness: Readiness,
                                @SerialName("startup")
                                val startup: Startup
                            ) {
                                @Serializable
                                data class Liveness(
                                    @SerialName("command")
                                    val command: List<String>,
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("type")
                                    val type: String
                                )

                                @Serializable
                                data class Readiness(
                                    @SerialName("command")
                                    val command: List<String>,
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("type")
                                    val type: String
                                )

                                @Serializable
                                data class Startup(
                                    @SerialName("command")
                                    val command: List<String>,
                                    @SerialName("enabled")
                                    val enabled: Boolean,
                                    @SerialName("type")
                                    val type: String
                                )
                            }

                            @Serializable
                            data class SecurityContext(
                                @SerialName("readOnlyRootFilesystem")
                                val readOnlyRootFilesystem: Boolean
                            )
                        }
                    }
                }
            }
        }

        @Serializable
        data class AdguardNetwork(
            @SerialName("enableDHCP")
            val enableDHCP: Boolean,
            @SerialName("webPort")
            val webPort: Int
        )

        @Serializable
        data class AdguardStorage(
            @SerialName("conf")
            val conf: Conf,
            @SerialName("work")
            val work: Work
        ) {
            @Serializable
            data class Conf(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("type")
                val type: String
            )

            @Serializable
            data class Work(
                @SerialName("datasetName")
                val datasetName: String,
                @SerialName("type")
                val type: String
            )
        }

        @Serializable
        data class Autodiscovery(
            @SerialName("enabled")
            val enabled: Boolean
        )

        @Serializable
        data class BroadcastProxyImage(
            @SerialName("pullPolicy")
            val pullPolicy: String,
            @SerialName("repository")
            val repository: String,
            @SerialName("tag")
            val tag: String
        )
    }

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
        val annotations: Annotations,
        @SerialName("kubeVersion")
        val kubeVersion: String,
        @SerialName("dependencies")
        val dependencies: List<Dependency>,
        @SerialName("type")
        val type: String,
        @SerialName("latest_chart_version")
        val latestChartVersion: String,
        @SerialName("sources")
        val sources: List<String>?,
        @SerialName("keywords")
        val keywords: List<String>?
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
        data class Annotations(
            @SerialName("title")
            val title: String?,
            @SerialName("truecharts.org/SCALE-support")
            val truechartsOrgSCALESupport: String?,
            @SerialName("truecharts.org/catagories")
            val truechartsOrgcatagories: String?,
            @SerialName("truecharts.org/grade")
            val truechartsOrggrade: String?
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

    @OptIn(InternalSerializationApi::class)
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
internal object PortalsSerializer : JsonContentPolymorphicSerializer<ChartRelease.Portals>(ChartRelease.Portals::class) {
    override fun selectDeserializer(element: JsonElement) = when {
        "Web Portal" in element.jsonObject -> ChartRelease.Portals.BadWebPortal.serializer()
        else -> ChartRelease.Portals.WebPortal.serializer()
    }
}
