package com.boswelja.truemanager.dashboard.configuration.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "dashboard_entry",
    primaryKeys = ["type", "server_id"]
)
internal data class DashboardEntryEntity(
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "server_id")
    val serverId: String,
    @ColumnInfo(name = "is_visible")
    val isVisible: Boolean,
    @ColumnInfo(name = "priority")
    val priority: Int
)
