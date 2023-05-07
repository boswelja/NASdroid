package com.boswelja.truemanager.dashboard.configuration.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_entry")
internal data class DashboardEntryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean,
    @ColumnInfo(name = "priority")
    val priority: Int
)
