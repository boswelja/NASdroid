package com.nasdroid.storage.logic

import com.nasdroid.storage.logic.pool.GetPoolDetails
import com.nasdroid.storage.logic.pool.GetPoolOverviews
import com.nasdroid.storage.logic.pool.import.GetImportablePools
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * A Koin module to inject the storage logic dependency graph. This depends on the API module.
 */
val StorageLogicModule = module {
    factoryOf(::GetImportablePools)

    factoryOf(::GetPoolDetails)
    factoryOf(::GetPoolOverviews)
}
