package com.boswelja.truemanager.storage

import com.boswelja.truemanager.storage.overview.StorageOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the storage dependency graph. This depends on the API module.
 */
val StorageModule = module {
    viewModelOf(::StorageOverviewViewModel)
}
