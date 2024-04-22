package com.nasdroid.reporting.logic.graph

import com.nasdroid.core.strongresult.StrongResult
import kotlinx.coroutines.flow.first

/**
 * Retrieves a list of network interfaces that are connected and functioning. You need to pass
 * network interfaces into [GetNetworkGraphs] to get graphs. See [invoke] for details.
 */
class GetNetworkInterfaces(
    private val getGraphIdentifiers: GetGraphIdentifiers
) {

    /**
     * Retrieves a list of network interfaces that are installed and functioning on the system, or a
     * [ReportingIdentifiersError] if something went wrong.
     */
    suspend operator fun invoke(): StrongResult<List<String>, ReportingIdentifiersError> {
        return getGraphIdentifiers("interface").first()
    }
}
