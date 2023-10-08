package com.nasdroid.dashboard.logic.dataloading.memory

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.capacity.Capacity.Companion.bytes
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMemoryUsageDataTest {

    private lateinit var mockReportingApi: ReportingV2Api

    private lateinit var getMemoryUsageData: GetMemoryUsageData

    @BeforeTest
    fun setUp() {
        mockReportingApi = mockk()

        getMemoryUsageData = GetMemoryUsageData(mockReportingApi)
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        coEvery {
            mockReportingApi.getGraphData(graphs = any(), start = any(), end = any())
        } throws HttpNotOkException(418, "I'm a teapot")

        val result = getMemoryUsageData()

        assertTrue(result.isFailure)
        assertEquals(
            HttpNotOkException(418, "I'm a teapot"),
            result.exceptionOrNull()
        )
    }

    @Test
    fun `when api returns data, data is returned`() = runTest {
        coEvery {
            mockReportingApi.getGraphData(graphs = any(), start = any(), end = any())
        } returns listOf(realMemoryData)

        val result = getMemoryUsageData()

        assertTrue(result.isSuccess)
        assertEquals(
            MemoryUsageData(
                used = 40326.bytes,
                free = 48345.bytes,
                cached = 40092.bytes
            ),
            result.getOrNull()
        )
    }

    companion object {
        private val realMemoryData = ReportingGraphData(
            name = "memory",
            identifier = null,
            data = listOf(
                listOf(
                    1696116241.0,
                    48351.9,
                    40319.88,
                    40092.33,
                    4.210938
                ),
                listOf(
                    1696116242.0,
                    48345.16,
                    40326.62,
                    40092.33,
                    4.210938
                )
            ),
            start = 0,
            end = 1,
            step = 1,
            legend = listOf(
                "time",
                "free",
                "used",
                "cached",
                "buffers"
            ),
            aggregations = null,
        )
    }
}
