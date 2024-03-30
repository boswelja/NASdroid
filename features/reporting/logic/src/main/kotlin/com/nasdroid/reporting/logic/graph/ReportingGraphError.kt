package com.nasdroid.reporting.logic.graph

sealed interface ReportingGraphError {

    data object InvalidGraphData : ReportingGraphError
}