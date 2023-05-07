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

    @Query("UPDATE dashboard_entry SET is_visible = 1 WHERE id = :entryId")
    suspend fun update(entryId: String, isVisible: Boolean)

    @Query("SELECT * FROM dashboard_entry WHERE priority >= :maxPriority")
    suspend fun getLowerPriority(maxPriority: Int): List<DashboardEntryEntity>

    @Query("SELECT * FROM dashboard_entry WHERE id = :entryId LIMIT 1")
    suspend fun get(entryId: String): DashboardEntryEntity

    @Query("SELECT * FROM dashboard_entry WHERE is_visible = 1 ORDER BY priority")
    fun getVisible(): Flow<List<DashboardEntryEntity>>

    @Query("SELECT * FROM dashboard_entry ORDER BY priority")
    fun getAll(): Flow<List<DashboardEntryEntity>>
}
