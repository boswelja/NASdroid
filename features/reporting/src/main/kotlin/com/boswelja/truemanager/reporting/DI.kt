package com.boswelja.truemanager.reporting

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val reportingModule = module {
    viewModelOf(::ReportingOverviewViewModel)
}