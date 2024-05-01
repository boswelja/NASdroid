package com.nasdroid.reporting.logic.graph

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

class GetSystemGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getSystemGraphs: GetSystemGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
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

        getSystemGraphs = GetSystemGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when graphs requested, then result is one item success`() = runTest {
        val result = getSystemGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    DurationGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "uptime",
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
