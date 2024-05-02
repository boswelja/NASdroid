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

class GetZfsGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getZfsGraphs: GetZfsGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()
        mockGetGraphData(reportingV2Api)

        getZfsGraphs = GetZfsGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `when graphs requested, then result is five item success`() = runTest {
        val result = getZfsGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    FloatGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "arcactualrate",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                        verticalLabel = "Events/s"
                    ),
                    FloatGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "arcrate",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                        verticalLabel = "Events/s"
                    ),
                    CapacityGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "arcsize",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                    ),
                    PercentageGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "arcresult",
                        identifier = "demand_data",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                    ),
                    PercentageGraph(
                        dataSlices = emptyList(),
                        legend = emptyList(),
                        name = "arcresult",
                        identifier = "prefetch_data",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                    )
                )
            ),
            result
        )
    }
}
