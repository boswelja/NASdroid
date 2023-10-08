package com.nasdroid.dashboard.logic.dataloading.network

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.reporting.ReportingGraph
import com.nasdroid.api.v2.reporting.ReportingV2Api
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetNetworkConfigurationTest {

    private lateinit var mockReportingApi: ReportingV2Api

    private lateinit var getNetworkConfiguration: GetNetworkConfiguration

    @BeforeTest
    fun setUp() {
        mockReportingApi = mockk()

        getNetworkConfiguration = GetNetworkConfiguration(mockReportingApi)
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        coEvery {
            mockReportingApi.getReportingGraphs(any(), any(), any())
        } throws HttpNotOkException(418, "I'm a teapot")

        val result = getNetworkConfiguration()

        assertTrue(result.isFailure)
        assertEquals(
            HttpNotOkException(418, "I'm a teapot"),
            result.exceptionOrNull()
        )
    }

    @Test
    fun `when api returns data, data is returned`() = runTest {
        coEvery {
            mockReportingApi.getReportingGraphs(any(), any(), any())
        } returns realGraphData

        val result = getNetworkConfiguration()

        assertTrue(result.isSuccess)
        assertEquals(
            NetworkConfiguration(
                listOf(
                    NetworkConfiguration.NetworkAdapter(
                        name = "eno1",
                        address = null
                    ),
                    NetworkConfiguration.NetworkAdapter(
                        name = "eno2",
                        address = null
                    )
                )
            ),
            result.getOrNull()
        )
    }

    companion object {
        private val realGraphData = listOf(
            ReportingGraph(
                name = "interface",
                title = "Interface Traffic ({identifier})",
                verticalLabel = "Bits/s",
                identifiers = listOf("eno1", "eno2"),
                stacked = false,
                stackedShowTotal = false
            ),
            ReportingGraph(
                name = "cpu",
                title = "CPU Usage",
                verticalLabel = "%CPU",
                identifiers = null,
                stacked = true,
                stackedShowTotal = false
            ),
        )
    }
}