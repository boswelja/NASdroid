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

class GetSystemGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getSystemGraphs: GetSystemGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        mockGetGraphData(reportingV2Api)

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
