package com.nasdroid

import android.app.Application
import com.nasdroid.apps.ui.AppsModule
import com.nasdroid.auth.ui.AuthModule
import com.nasdroid.dashboard.ui.DashboardModule
import com.nasdroid.storage.ui.StorageUiModule
import com.nasdroid.api.v2.ApiV2Module
import com.nasdroid.power.ui.PowerModule
import com.nasdroid.reporting.ui.ReportingModule
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
            modules(ApiV2Module)

            modules(
                AppsModule,
                AuthModule,
                DashboardModule,
                PowerModule,
                ReportingModule,
                StorageUiModule,
            )
        }
    }
}
