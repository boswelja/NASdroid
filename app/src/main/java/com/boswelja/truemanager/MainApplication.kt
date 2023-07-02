package com.boswelja.truemanager

import android.app.Application
import com.boswelja.truemanager.auth.AuthModule
import com.boswelja.truemanager.core.api.v2.ApiV2Module
import com.boswelja.truemanager.apps.AppsModule
import com.boswelja.truemanager.dashboard.ui.DashboardModule
import com.boswelja.truemanager.reporting.ReportingModule
import com.boswelja.truemanager.storage.StorageModule
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
                ReportingModule,
                StorageModule,
            )
        }
    }
}
