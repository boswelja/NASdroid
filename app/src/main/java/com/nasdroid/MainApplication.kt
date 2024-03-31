package com.nasdroid

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * An implementation of [Application] that initializes Koin for the app.
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(NasDroidModule)
        }
    }
}
