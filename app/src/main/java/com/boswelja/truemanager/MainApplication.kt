package com.boswelja.truemanager

import android.app.Application
import com.boswelja.truemanager.auth.authModule
import com.boswelja.truemanager.core.api.v2.apiV2Module
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(apiV2Module)

            modules(
                authModule,
            )
        }
    }
}