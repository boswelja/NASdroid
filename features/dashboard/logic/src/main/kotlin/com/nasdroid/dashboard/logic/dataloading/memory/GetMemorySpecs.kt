package com.nasdroid.dashboard.logic.dataloading.memory

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.capacity.Capacity
import com.nasdroid.capacity.Capacity.Companion.bytes

class GetMemorySpecs(
    private val systemV2Api: SystemV2Api,
) {

    suspend operator fun invoke(): Result<MemorySpecs> {
        return try {
            val systemInformation = systemV2Api.getSystemInfo()
            Result.success(
                MemorySpecs(
                    isEcc = systemInformation.eccMemory,
                    totalCapacity = systemInformation.physicalMemory.bytes
                )
            )
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}

data class MemorySpecs(
    val isEcc: Boolean,
    val totalCapacity: Capacity
)
