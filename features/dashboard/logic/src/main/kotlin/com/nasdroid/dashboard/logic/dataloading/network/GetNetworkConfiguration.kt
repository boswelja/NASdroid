package com.nasdroid.dashboard.logic.dataloading.network

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingV2Api

/**
 * Get a simple representation of the system network configuration. See [invoke] for details.
 */
class GetNetworkConfiguration(
    private val reportingV2Api: ReportingV2Api,
) {

    /**
     * Returns a [Result] that contains either [NetworkConfiguration], or an exception if the
     * request failed.
     */
    suspend operator fun invoke(): Result<NetworkConfiguration> {
        return try {
            val adapterNames = reportingV2Api.getReportingGraphs(
                limit = null,
                offset = null,
                sort = null
            ).first { it.name == INTERFACE_GRAPH_NAME }.identifiers
            val adapters = adapterNames!!.map {
                NetworkConfiguration.NetworkAdapter(
                    name = it,
                    address = null // TODO Get the address for an adapter
                )
            }
            Result.success(NetworkConfiguration(adapters = adapters))
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }

    companion object {
        private const val INTERFACE_GRAPH_NAME = "interface"
    }
}

/**
 * Describes the network configuration of the system.
 *
 * @property adapters A list of [NetworkAdapter]s the system has.
 */
data class NetworkConfiguration(
    val adapters: List<NetworkAdapter>
) {

    /**
     * Describes a single network adapter in the system.
     *
     * @property name The unique adapter name.
     * @property address The adapter IP address, if any.
     */
    data class NetworkAdapter(
        val name: String,
        val address: String?
    )
}
