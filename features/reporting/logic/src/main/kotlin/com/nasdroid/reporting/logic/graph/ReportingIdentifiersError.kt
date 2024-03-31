package com.nasdroid.reporting.logic.graph

/**
 * Encapsulates all possible errors that could occur when retrieving identifiers for reporting graph
 * variants.
 */
sealed interface ReportingIdentifiersError {

    /**
     * Indicates that the graph that was requested does not exist, or it had no identifiers.
     */
    data object NoGroupFound : ReportingIdentifiersError
}
