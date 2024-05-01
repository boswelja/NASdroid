package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.exception.ClientRequestException
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.api.v2.reporting.RequestedGraph
import com.nasdroid.api.v2.reporting.Units
import com.nasdroid.core.strongresult.StrongResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetMemoryGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getMemoryGraphs: GetMemoryGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        coEvery {
            reportingV2Api.getGraphData(emptyList(), Units.HOUR, 1)
        } throws ClientRequestException(422, "Parameter graphs is required")
        coEvery {
            reportingV2Api.getGraphData(not(emptyList()), Units.HOUR, 1)
        } answers {
            arg<List<RequestedGraph>>(0).map {
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
        }

        getMemoryGraphs = GetMemoryGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when one disk is requested, then result is two item success`() = runTest {
        val result = getMemoryGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    CapacityGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "memory",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    ),
                    CapacityGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "swap",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    )
                )
            ),
            result
        )
    }
}
