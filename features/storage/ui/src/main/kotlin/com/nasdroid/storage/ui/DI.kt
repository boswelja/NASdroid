package com.nasdroid.storage.ui

import com.nasdroid.storage.logic.StorageLogicModule
import com.nasdroid.storage.ui.overview.StorageOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * A Koin module to inject the storage dependency graph. This depends on the API module.
 */
val StorageUiModule = module {
    loadKoinModules(StorageLogicModule)

    viewModelOf(::StorageOverviewViewModel)
}
