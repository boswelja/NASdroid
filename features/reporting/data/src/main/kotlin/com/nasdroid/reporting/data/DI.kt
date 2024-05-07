package com.nasdroid.reporting.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.nasdroid.reporting.data.metadata.GraphMetadataCache
import com.nasdroid.reporting.data.metadata.InMemoryGraphMetadataCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * A Koin module that provides implementations of apps data interfaces.
 */
val ReportingDataModule = module {
    single {
        // By not specifying a name, the database should be created in-memory.
        val driver: SqlDriver = AndroidSqliteDriver(ReportingDatabase.Schema, get(), null)
        ReportingDatabase(driver)
    }
    singleOf(::InMemoryGraphMetadataCache) bind GraphMetadataCache::class
}
