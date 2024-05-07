package com.nasdroid.reporting.logic.graph

import com.nasdroid.core.strongresult.StrongResult
import kotlinx.coroutines.flow.first

/**
 * Retrieves a list of disk identifiers that are installed and functioning. You need to pass
 * disk identifiers into [GetDiskGraphs] to get graphs. See [invoke] for details.
 */
class GetDisks(
    private val getGraphIdentifiers: GetGraphIdentifiers
) {

    /**
     * Retrieves a list of disk identifiers that are installed and functioning on the system, or a
     * [ReportingIdentifiersError] if something went wrong.
     */
    suspend operator fun invoke(): StrongResult<List<String>, ReportingIdentifiersError> {
        return getGraphIdentifiers("disk").first()
    }
}
