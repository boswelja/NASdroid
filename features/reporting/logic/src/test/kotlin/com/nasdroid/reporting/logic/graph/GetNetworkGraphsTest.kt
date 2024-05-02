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

class GetNetworkGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getNetworkGraphs: GetNetworkGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        mockGetGraphData(reportingV2Api)

        getNetworkGraphs = GetNetworkGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when zero interfaces are requested, then result is empty success`() = runTest {
        val result = getNetworkGraphs(emptyList())

        assertEquals(StrongResult.success(emptyList()), result)
    }

    @Test
    fun `when one interface is requested, then result is one item success`() = runTest {
        val result = getNetworkGraphs(listOf("eno1"))

        assertEquals(
            StrongResult.success(
                listOf(
                    BitrateGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "interface",
                        identifier = "eno1",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    )
                )
            ),
            result
        )
    }
}