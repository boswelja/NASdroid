package com.boswelja.truemanager.reporting

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val ReportingModule = module {
    viewModelOf(::ReportingOverviewViewModel)
}
