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

class GetMemoryGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getMemoryGraphs: GetMemoryGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        mockGetGraphData(reportingV2Api)

        getMemoryGraphs = GetMemoryGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when graphs are requested, then result is two item success`() = runTest {
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
