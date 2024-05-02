package com.nasdroid.reporting.logic.graph

import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.DEFAULT_VALID_DATA
import com.nasdroid.reporting.logic.mockInvalidGetGraphData
import com.nasdroid.reporting.logic.mockValidGetGraphData
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class GetSystemGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getSystemGraphs: GetSystemGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()

        getSystemGraphs = GetSystemGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `given data is valid, when graphs requested, then result is one item success`() = runTest {
        mockValidGetGraphData(reportingV2Api)

        val result = getSystemGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    DurationGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.seconds }
                            )
                        },
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

    @Test
    fun `given invalid data, when graphs requested, then error invalid data returned`() = runTest {
        mockInvalidGetGraphData(reportingV2Api)

        val result = getSystemGraphs()

        assertEquals(
            StrongResult.failure<List<Graph<*>>, ReportingGraphError>(ReportingGraphError.InvalidGraphData),
            result
        )
    }
}
