package com.boswelja.truemanager.dashboard.configuration.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
internal interface DashboardEntryDao {

    @Insert
    suspend fun add(entries: List<DashboardEntryEntity>)

    @Delete
    suspend fun remove(vararg entries: DashboardEntryEntity)

    @Update
    suspend fun update(entries: List<DashboardEntryEntity>)

    @Query("UPDATE dashboard_entry SET is_visible = :isVisible WHERE id = :entryId AND server_id = :serverId")
    suspend fun update(serverId: String, entryId: String, isVisible: Boolean)

    @Query("SELECT * FROM dashboard_entry WHERE priority >= :maxPriority AND server_id = :serverId")
    suspend fun getLowerPriority(serverId: String, maxPriority: Int): List<DashboardEntryEntity>

    @Query("SELECT * FROM dashboard_entry WHERE id = :entryId AND server_id = :serverId LIMIT 1")
    suspend fun get(serverId: String, entryId: String): DashboardEntryEntity

    @Query("SELECT * FROM dashboard_entry WHERE is_visible = 1 AND server_id = :serverId ORDER BY priority")
    fun getVisible(serverId: String): Flow<List<DashboardEntryEntity>>

    @Query("SELECT * FROM dashboard_entry ORDER BY priority")
    fun getAll(): Flow<List<DashboardEntryEntity>>
}
