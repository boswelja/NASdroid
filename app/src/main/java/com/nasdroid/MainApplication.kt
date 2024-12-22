package com.nasdroid

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinAppDeclaration

/**
 * An implementation of [Application] that initializes Koin for the app.
 */
@OptIn(KoinExperimentalAPI::class)
class MainApplication : Application(), KoinStartup {

    override fun onKoinStartup(): KoinAppDeclaration = {
        androidContext(this@MainApplication)
        modules(NasDroidModule)
    }
}
