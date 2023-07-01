package com.boswelja.truemanager.data.configuration.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DashboardEntryEntity::class], version = 1)
internal abstract class DashboardEntryDatabase : RoomDatabase() {

    abstract fun getDashboardEntryDao(): DashboardEntryDao
}
