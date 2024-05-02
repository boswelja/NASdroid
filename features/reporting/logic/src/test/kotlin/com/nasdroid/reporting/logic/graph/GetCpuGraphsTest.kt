package com.nasdroid.reporting.logic.graph

import com.boswelja.percentage.Percentage.Companion.percent
import com.boswelja.temperature.Temperature.Companion.celsius
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.DEFAULT_END_SECONDS
import com.nasdroid.reporting.logic.DEFAULT_START_SECONDS
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

class GetCpuGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getCpuGraphs: GetCpuGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()

        getCpuGraphs = GetCpuGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `given data is valid, when graphs are requested, then result is three item success`() = runTest {
        mockValidGetGraphData(reportingV2Api)

        val result = getCpuGraphs()

        assertEquals(
            StrongResult.success(
                listOf(
                    PercentageGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.percent }
                            )
                        },
                        legend = emptyList(),
                        name = "cpu",
                        identifier = null,
                        start = Instant.fromEpochSeconds(DEFAULT_START_SECONDS),
                        end = Instant.fromEpochSeconds(DEFAULT_END_SECONDS)
                    ),
                    TemperatureGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.celsius }
                            )
                        },
                        legend = emptyList(),
                        name = "cputemp",
                        identifier = null,
                        start = Instant.fromEpochSeconds(DEFAULT_START_SECONDS),
                        end = Instant.fromEpochSeconds(DEFAULT_START_SECONDS)
                    ),
                    FloatGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.toFloat() }
                            )
                        },
                        legend = emptyList(),
                        name = "load",
                        identifier = null,
                        start = Instant.fromEpochSeconds(DEFAULT_START_SECONDS),
                        end = Instant.fromEpochSeconds(DEFAULT_END_SECONDS),
                        verticalLabel = "Processes"
                    )
                )
            ),
            result
        )
    }

    @Test
    fun `given invalid data, when graphs requested, then error invalid data returned`() = runTest {
        mockInvalidGetGraphData(reportingV2Api)

        val result = getCpuGraphs()

        assertEquals(
            StrongResult.failure<List<Graph<*>>, ReportingGraphError>(ReportingGraphError.InvalidGraphData),
            result
        )
    }
}
