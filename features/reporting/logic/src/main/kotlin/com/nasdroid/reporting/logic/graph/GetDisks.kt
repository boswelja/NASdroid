package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult

/**
 * Retrieves a list of disk identifiers that are installed and functioning. You need to pass
 * disk identifiers into [GetDiskGraphs] to get graphs. See [invoke] for details.
 */
class GetDisks(
    private val reportingV2Api: ReportingV2Api
) {

    /**
     * Retrieves a list of disk identifiers that are installed and functioning on the system, or a
     * [ReportingIdentifiersError] if something went wrong.
     */
    suspend operator fun invoke(): StrongResult<List<String>, ReportingIdentifiersError> {
        val interfaces = reportingV2Api.getReportingGraphs(null, null, null)
            .firstOrNull { it.name == "disk" }
            ?.identifiers
        return if (interfaces == null) {
            StrongResult.failure(ReportingIdentifiersError.NoGroupFound)
        } else {
            StrongResult.success(interfaces)
        }
    }
}
