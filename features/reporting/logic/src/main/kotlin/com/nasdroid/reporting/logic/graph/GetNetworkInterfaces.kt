package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult

/**
 * Retrieves a list of network interfaces that are connected and functioning. You need to pass
 * network interfaces into [GetNetworkGraphs] to get graphs. See [invoke] for details.
 */
class GetNetworkInterfaces(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a list of network interfaces that are installed and functioning on the system, or a
     * [ReportingIdentifiersError] if something went wrong.
     */
    suspend operator fun invoke(): StrongResult<List<String>, ReportingIdentifiersError> {
        val interfaces = reportingV2Api.getReportingGraphs(null, null, null)
            .firstOrNull { it.name == "interface" }
            ?.identifiers
        return if (interfaces == null) {
            StrongResult.failure(ReportingIdentifiersError.NoGroupFound)
        } else {
            StrongResult.success(interfaces)
        }
    }
}
