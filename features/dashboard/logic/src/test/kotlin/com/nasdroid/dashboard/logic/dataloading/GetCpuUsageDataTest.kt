package com.nasdroid.dashboard.logic.dataloading

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.exception.ServerResponseException
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuData
import com.nasdroid.dashboard.logic.dataloading.cpu.CpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuSpecs
import com.nasdroid.dashboard.logic.dataloading.cpu.GetCpuUsageData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCpuUsageDataTest {

    private lateinit var mockCpuSpecs: GetCpuSpecs
    private lateinit var mockReportingApi: ReportingV2Api

    private lateinit var getCpuUsageData: GetCpuUsageData

    @BeforeTest
    fun setUp() {
        mockCpuSpecs = mockk()
        mockReportingApi = mockk()

        getCpuUsageData = GetCpuUsageData(mockCpuSpecs, mockReportingApi)
    }

    @Test
    fun `when cpu specs fail, error is returned`() = runTest {
        coEvery { mockCpuSpecs() } returns Result.failure(HttpNotOkException(418, "I'm a teapot"))

        val result = getCpuUsageData().first()

        assertTrue(result.isFailure)
        assertEquals(
            HttpNotOkException(418, "I'm a teapot"),
            result.exceptionOrNull()
        )
    }

    @Test
    fun `when reporting throws, error is returned`() = runTest {
        coEvery { mockCpuSpecs() } returns Result.success(CpuSpecs("Ampere", 128, 80))
        coEvery {
            mockReportingApi.getGraphData(graphs = any(), start = any(), end = any())
        } throws ServerResponseException(500, "Internal server error")

        val result = getCpuUsageData().first()

        assertTrue(result.isFailure)
        assertEquals(
            ServerResponseException(500, "Internal server error"),
            result.exceptionOrNull()
        )
    }

    @Test
    fun `when reporting returns data, usage data is returned`() = runTest {
        coEvery { mockReportingApi.getGraphData(graphs = any(), start = any(), end = any()) } returns listOf(
            realCpuData,
            realTemperatureData
        )

        val result = getCpuUsageData(CpuSpecs("Ampere", 80, 128))

        assertTrue(result.isSuccess)
        assertEquals(
            CpuData(
                model = "Ampere",
                physicalCores = 80,
                totalCores = 128,
                utilisation = ((100 - 98.33899) / 100).toFloat(),
                temp = 28
            ),
            result.getOrNull()
        )
    }

    companion object {
        private val realCpuData = ReportingGraphData(
            name = "cpu",
            identifier = null,
            data = listOf(
                listOf(
                    1696116241.0,
                    0.0178987,
                    0.9486307,
                    0.3758726,
                    0.0178987,
                    0.0,
                    98.6218
                ),
                listOf(
                    1696116242.0,
                    0.0178603,
                    1.2145026,
                    0.3929273,
                    0.0,
                    0.0178603,
                    98.33899
                )
            ),
            start = 0,
            end = 1,
            step = 1,
            legend = listOf(
                "time",
                "softirq",
                "user",
                "system",
                "nice",
                "iowait",
                "idle"
            ),
            aggregations = null,
        )

        private val realTemperatureData =  ReportingGraphData(
            name = "cputemp",
            identifier = null,
            data = listOf(
                listOf(
                    1696116241.0,
                    18.0,
                    18.0,
                    19.0,
                    20.0,
                ),
                listOf(
                    1696116242.0,
                    23.0,
                    24.0,
                    28.0,
                    25.0,
                ),
            ),
            start = 0,
            end = 1,
            step = 1,
            legend = listOf(
                "time",
                "0",
                "1",
                "2",
                "3",
                "4",
            ),
            aggregations = null,
        )
    }
}
