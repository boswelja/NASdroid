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
import kotlin.test.assertEquals

class GetCpuGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getCpuGraphs: GetCpuGraphs

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

        getCpuGraphs = GetCpuGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when one disk is requested, then result is two item success`() = runTest {
        val result = getCpuGraphs()

        assertEquals(
            StrongResult.success(
                listOf(
                    PercentageGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "cpu",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    ),
                    TemperatureGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "cputemp",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    ),
                    FloatGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "load",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                        verticalLabel = "Processes"
                    )
                )
            ),
            result
        )
    }
}