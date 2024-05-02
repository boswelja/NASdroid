package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.mockGetGraphData
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
        mockGetGraphData(reportingV2Api)

        getCpuGraphs = GetCpuGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when graphs are requested, then result is three item success`() = runTest {
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