package com.nasdroid.dashboard.logic.dataloading.memory

import com.boswelja.capacity.Capacity.Companion.gigabytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.api.exception.ServerResponseException
import com.nasdroid.api.v2.system.SystemInfo
import com.nasdroid.api.v2.system.SystemV2Api
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetMemorySpecsTest {

    private lateinit var mockSystemApi: SystemV2Api

    private lateinit var getMemorySpecs: GetMemorySpecs

    @BeforeTest
    fun setUp() {
        mockSystemApi = mockk()

        getMemorySpecs = GetMemorySpecs(mockSystemApi)
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        coEvery { mockSystemApi.getSystemInfo() } throws ServerResponseException(500, "Internal server error")

        val result = getMemorySpecs()

        assertTrue(result.isFailure)
        assertEquals(
            ServerResponseException(500, "Internal server error"),
            result.exceptionOrNull()
        )
    }

    @Test
    fun `when api returns data, data is returned`() = runTest {
        coEvery { mockSystemApi.getSystemInfo() } returns SystemInfo(
            version = "2023.1.2",
            buildTime = Long.MAX_VALUE,
            hostName = "TrueNAS System",
            physicalMemory = 12.gigabytes.toLong(CapacityUnit.BYTE),
            cpuModel = "Ampere",
            cores = 128,
            physicalCores = 80,
            loadAvg = emptyList(),
            uptime = "12345",
            uptimeSeconds = 12345.0,
            systemSerial = "serial",
            systemProduct = "TrueNAS SCALE",
            systemProductVersion = "2023.1.2",
            license = null,
            bootTime = Long.MIN_VALUE,
            dateTime = Long.MAX_VALUE,
            birthday = null,
            timeZone = "GMT",
            systemManufacturer = "Manufacturer",
            eccMemory = true,
        )

        val result = getMemorySpecs()

        assertTrue(result.isSuccess)
        assertEquals(
            MemorySpecs(
                isEcc = true,
                totalCapacity = 12.gigabytes
            ),
            result.getOrNull()
        )
    }
}
