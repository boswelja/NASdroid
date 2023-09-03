package com.nasdroid.auth.data.serverstore.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface AuthenticatedServerDao {
    @Query("SELECT * FROM authenticated_servers")
    fun getAll(): Flow<List<AuthenticatedServerDto>>

    @Query("SELECT * FROM authenticated_servers WHERE host_id = :id")
    suspend fun get(id: String): AuthenticatedServerDto

    @Insert
    suspend fun insertAll(vararg servers: AuthenticatedServerDto)

    @Delete
    suspend fun delete(server: AuthenticatedServerDto)
}
