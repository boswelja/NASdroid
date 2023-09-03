package com.nasdroid.auth.data.serverstore.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AuthenticatedServerDto::class], version = 2)
internal abstract class AuthenticatedServerDatabase : RoomDatabase() {
    abstract fun authenticatedServerDao(): AuthenticatedServerDao
}
