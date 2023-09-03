package com.nasdroid.auth.data.serverstore.sqldelight

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

/**
 * Creates an instance of [AuthDatabase] that will be stored persistently.
 */
fun createAuthDatabase(context: Context): AuthDatabase {
    val driver: SqlDriver = AndroidSqliteDriver(AuthDatabase.Schema, context, "auth.db")
    return AuthDatabase(driver)
}
