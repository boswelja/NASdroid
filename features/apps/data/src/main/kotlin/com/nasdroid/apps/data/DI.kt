package com.nasdroid.apps.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.nasdroid.apps.data.installed.InMemoryInstalledAppCache
import com.nasdroid.apps.data.installed.InstalledAppCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module that provides implementations of apps data interfaces.
 */
val AppsDataModule = module {
    single {
        // By not specifying a name, the database should be created in-memory.
        val driver: SqlDriver = AndroidSqliteDriver(AppsDatabase.Schema, get(), null)
        AppsDatabase(driver)
    }
    singleOf(::InMemoryInstalledAppCache) bind InstalledAppCache::class
}
