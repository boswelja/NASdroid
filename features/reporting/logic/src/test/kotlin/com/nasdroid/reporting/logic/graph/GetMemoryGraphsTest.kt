package com.nasdroid.reporting.logic.graph

import com.boswelja.capacity.Capacity.Companion.mebibytes
import com.nasdroid.api.v2.reporting.ReportingV2Api
import com.nasdroid.core.strongresult.StrongResult
import com.nasdroid.reporting.logic.DEFAULT_END_SECONDS
import com.nasdroid.reporting.logic.DEFAULT_START_SECONDS
import com.nasdroid.reporting.logic.DEFAULT_VALID_DATA
import com.nasdroid.reporting.logic.mockValidGetGraphData
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetMemoryGraphsTest {

    private lateinit var reportingV2Api: ReportingV2Api

    private lateinit var getMemoryGraphs: GetMemoryGraphs

    @BeforeTest
    fun setUp() {
        reportingV2Api = mockk()

        getMemoryGraphs = GetMemoryGraphs(reportingV2Api, Dispatchers.Default)
    }

    @Test
    fun `given data is valid, when graphs are requested, then result is two item success`() = runTest {
        mockValidGetGraphData(reportingV2Api)

        val result = getMemoryGraphs()

        kotlin.test.assertEquals(
            StrongResult.success(
                listOf(
                    CapacityGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.mebibytes }
                            )
                        },
                        legend = emptyList(),
                        name = "memory",
                        identifier = null,
                        start = Instant.fromEpochSeconds(DEFAULT_START_SECONDS),
                        end = Instant.fromEpochSeconds(DEFAULT_END_SECONDS)
                    ),
                    CapacityGraph(
                        dataSlices = DEFAULT_VALID_DATA.map {
                            Graph.DataSlice(
                                timestamp = Instant.fromEpochSeconds(it.first().toLong()),
                                data = it.drop(1).map { it.mebibytes }
                            )
                        },
                        legend = emptyList(),
                        name = "swap",
                        identifier = null,
                        start = Instant.fromEpochSeconds(DEFAULT_START_SECONDS),
                        end = Instant.fromEpochSeconds(DEFAULT_END_SECONDS)
                    )
                )
            ),
            result
        )
    }
}
