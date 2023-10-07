package com.nasdroid.dashboard.logic.dataloading.cpu

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api

/**
 * Retrieves specifications for the CPU(s) installed in the system. See [invoke] for details.
 */
class GetCpuSpecs(
    private val systemV2Api: SystemV2Api,
) {

    /**
     * Returns a Result containing either a [CpuSpecs], or an expected failure.
     */
    suspend operator fun invoke(): Result<CpuSpecs> {
        return try {
            val systemInformation = systemV2Api.getSystemInfo()
            Result.success(
                    CpuSpecs(
                    model = systemInformation.cpuModel,
                    physicalCores = systemInformation.physicalCores,
                    totalCores = systemInformation.cores,
                )
            )
        } catch (e: HttpNotOkException) {
            Result.failure(e)
        }
    }
}

/**
 * Specifications for a CPU.
 *
 * @property model The CPU model. For example, "Intel Xeon E4-2680".
 * @property physicalCores The total number of physical CPU cores, excluding threads.
 * @property totalCores The total number of cores present, including threads.
 */
data class CpuSpecs(
    val model: String,
    val physicalCores: Int,
    val totalCores: Int,
)
