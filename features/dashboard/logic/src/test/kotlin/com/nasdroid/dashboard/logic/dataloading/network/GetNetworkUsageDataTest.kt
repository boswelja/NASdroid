package com.nasdroid.dashboard.logic.dataloading.network

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingGraphData
import com.nasdroid.api.v2.reporting.ReportingV2Api
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.math.roundToLong
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetNetworkUsageDataTest {

    private lateinit var mockReportingApi: ReportingV2Api

    private lateinit var getNetworkUsageData: GetNetworkUsageData

    @BeforeTest
    fun setUp() {
        mockReportingApi = mockk()

        getNetworkUsageData = GetNetworkUsageData(mockReportingApi)
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        coEvery {
            mockReportingApi.getGraphData(graphs = any(), start = any(), end = any())
        } throws HttpNotOkException(418, "I'm a teapot")

        val result = getNetworkUsageData(listOf("eno1", "eno2"))

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
        } returns realNetworkData

        val result = getNetworkUsageData(listOf("eno1", "eno2"))

        assertTrue(result.isSuccess)
        assertEquals(
            NetworkUsageData(
                adapterUtilisation = listOf(
                    NetworkUsageData.AdapterUtilisation(
                        name = "eno1",
                        receivedBits = (17.33774 * 1000).roundToLong(),
                        sentBits = (4.876272 * 1000).roundToLong()
                    ),
                    NetworkUsageData.AdapterUtilisation(
                        name = "eno2",
                        receivedBits = (17.33774 * 1000).roundToLong(),
                        sentBits = (4.876272 * 1000).roundToLong()
                    )
                )
            ),
            result.getOrNull()
        )
    }

    companion object {
        private val realNetworkData = listOf(
            ReportingGraphData(
                name = "interface",
                identifier = "eno1",
                data = listOf(
                    listOf(
                        1696116241.0,
                        35.93198,
                        15.426357
                    ),
                    listOf(
                        1696116242.0,
                        17.33774,
                        4.876272
                    )
                ),
                start = 0,
                end = 1,
                step = 1,
                legend = listOf(
                    "time",
                    "received",
                    "sent"
                ),
                aggregations = null
            ),
            ReportingGraphData(
                name = "interface",
                identifier = "eno2",
                data = listOf(
                    listOf(
                        1696116241.0,
                        35.93198,
                        15.426357
                    ),
                    listOf(
                        1696116242.0,
                        17.33774,
                        4.876272
                    )
                ),
                start = 0,
                end = 1,
                step = 1,
                legend = listOf(
                    "time",
                    "received",
                    "sent"
                ),
                aggregations = null
            )
        )
    }
}
