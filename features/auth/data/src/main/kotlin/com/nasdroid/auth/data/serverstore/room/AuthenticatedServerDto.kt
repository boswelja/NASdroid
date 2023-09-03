package com.nasdroid.auth.data.serverstore.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authenticated_servers")
internal data class AuthenticatedServerDto(
    @PrimaryKey
    @ColumnInfo("host_id")
    val hostId: String,
    @ColumnInfo(name = "server_address")
    val serverAddress: String,
    @ColumnInfo(name = "token")
    val token: String,
    @ColumnInfo("name")
    val name: String,
)
