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

class GetDiskGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getDiskGraphs: GetDiskGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        mockGetGraphData(reportingV2Api)

        getDiskGraphs = GetDiskGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when zero disks are requested, then result is empty success`() = runTest {
        val result = getDiskGraphs(emptyList())

        assertEquals(StrongResult.success(emptyList()), result)
    }

    @Test
    fun `when one disk is requested, then result is two item success`() = runTest {
        val result = getDiskGraphs(listOf("sda"))

        assertEquals(
            StrongResult.success(
                listOf(
                    CapacityGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "disk",
                        identifier = "sda",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    ),
                    TemperatureGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "disktemp",
                        identifier = "sda",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421)
                    )
                )
            ),
            result
        )
    }
}
