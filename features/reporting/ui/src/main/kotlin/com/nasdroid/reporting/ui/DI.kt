package com.nasdroid.reporting.ui

import com.nasdroid.reporting.logic.ReportingLogicModule
import com.nasdroid.reporting.ui.overview.ReportingOverviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * A Koin module to inject the reporting dependency graph. This depends on the API module.
 */
val ReportingModule = module {
    includes(ReportingLogicModule)

    viewModelOf(::ReportingOverviewViewModel)
}
