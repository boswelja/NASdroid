package com.boswelja.truemanager.auth

import com.boswelja.truemanager.auth.ui.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)
}
