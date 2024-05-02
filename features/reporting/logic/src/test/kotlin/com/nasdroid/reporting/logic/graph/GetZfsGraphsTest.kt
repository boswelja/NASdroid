package com.nasdroid.reporting.logic.graph

import com.boswelja.capacity.Capacity.Companion.mebibytes
import com.boswelja.percentage.Percentage.Companion.percent
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

class GetZfsGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getZfsGraphs: GetZfsGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()

        getZfsGraphs = GetZfsGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `given data is valid, when graphs requested, then result is five item success`() = runTest {
        mockValidGetGraphData(reportingV2Api)

        val result = getZfsGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    FloatGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.toFloat() }
                            )
                        },
                        legend = emptyList(),
                        name = "arcactualrate",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                        verticalLabel = "Events/s"
                    ),
                    FloatGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.toFloat() }
                            )
                        },
                        legend = emptyList(),
                        name = "arcrate",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                        verticalLabel = "Events/s"
                    ),
                    CapacityGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.mebibytes }
                            )
                        },
                        legend = emptyList(),
                        name = "arcsize",
                        identifier = null,
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                    ),
                    PercentageGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.percent }
                            )
                        },
                        legend = emptyList(),
                        name = "arcresult",
                        identifier = "demand_data",
                        start = Instant.fromEpochSeconds(1714556421),
                        end = Instant.fromEpochSeconds(1714556421),
                    ),
                    PercentageGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.percent }
                            )
                        },
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

    @Test
    fun `given invalid data, when graphs requested, then error invalid data returned`() = runTest {
        mockInvalidGetGraphData(reportingV2Api)

        val result = getZfsGraphs()

        assertEquals(
            StrongResult.failure<List<Graph<*>>, ReportingGraphError>(ReportingGraphError.InvalidGraphData),
            result
        )
    }
}
