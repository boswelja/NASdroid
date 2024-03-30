package com.nasdroid.reporting.logic.graph

/**
 * Encapsulates all possible errors that could occur when retrieving reporting graph data.
 */
sealed interface ReportingGraphError {

    /**
     * Indicates that the data sent by the server is not valid, or we don't know how to handle it.
     */
    data object InvalidGraphData : ReportingGraphError
}
