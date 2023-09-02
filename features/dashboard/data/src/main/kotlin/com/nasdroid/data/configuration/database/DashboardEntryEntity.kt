package com.nasdroid.data.configuration.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_entry")
internal data class DashboardEntryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("uid")
    val uid: Long,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "server_id")
    val serverId: String,
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean,
    @ColumnInfo(name = "priority")
    val priority: Int
)
