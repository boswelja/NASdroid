package com.nasdroid.storage.logic.pool

import com.nasdroid.api.v2.pool.Pool
import com.nasdroid.api.v2.pool.PoolV2Api

/**
 * Gets a list of all pools and their status. See [invoke] for details.
 */
class GetPools(
    private val poolV2Api: PoolV2Api
) {

    /**
     * Retrieves a list of [Pool]s from the server.
     */
    suspend operator fun invoke(): List<Pool> {
        return poolV2Api.getPools()
    }
}
