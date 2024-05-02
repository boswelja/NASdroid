package com.nasdroid.reporting.logic

import com.nasdroid.api.exception.ClientRequestException
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import io.mockk.coEvery

fun mockGetGraphData(
    target: ReportingV2Api,
    dataProducer: (RequestedGraph) -> ReportingGraphData = {
        ReportingGraphData(
            name = it.name,
            identifier = it.identifier,
            data = emptyList(),
            start = 1714556421,
            end = 1714556421,
            step = null,
            legend = emptyList(),
            aggregations = null
        )
    }
) {
    coEvery {
        target.getGraphData(emptyList(), Units.HOUR, 1)
    } throws ClientRequestException(422, "Parameter graphs is required")
    coEvery {
        target.getGraphData(not(emptyList()), Units.HOUR, 1)
    } answers {
        arg<List<RequestedGraph>>(0).map(dataProducer)
    }
}