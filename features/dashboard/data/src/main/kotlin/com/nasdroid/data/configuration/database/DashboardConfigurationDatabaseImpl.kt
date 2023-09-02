package com.nasdroid.data.configuration.database

import android.content.Context
import androidx.room.Room
import com.nasdroid.data.configuration.DashboardConfiguration
import com.nasdroid.data.configuration.DashboardEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

/**
 * An implementation of [DashboardConfiguration] backed by a Room database.
 */
class DashboardConfigurationDatabaseImpl(
    context: Context
) : DashboardConfiguration {
    private val database = Room.databaseBuilder(
        context,
        DashboardEntryDatabase::class.java,
        "dashboard-configuration"
    ).fallbackToDestructiveMigration().build()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getVisibleEntries(serverId: String): Flow<List<DashboardEntry>> = database.getDashboardEntryDao()
        .getVisibleFor(serverId)
        .mapLatest {
            it.map { entity -> entity.toDashboardEntryEntry() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllEntries(serverId: String): Flow<List<DashboardEntry>> = database.getDashboardEntryDao()
        .getAll()
        .mapLatest {
            it.map { entity -> entity.toDashboardEntryEntry() }
        }

    override suspend fun update(items: List<DashboardEntry>) {
        database.getDashboardEntryDao().update(items.map { it.toDashboardEntryEntity() })
    }

    override suspend fun insertEntries(entries: List<DashboardEntry>) {
        database.getDashboardEntryDao().add(
            entries.map { it.toDashboardEntryEntity() }
        )
    }

    override suspend fun hasAnyEntries(serverId: String): Boolean {
        return database.getDashboardEntryDao().getAll().first().isNotEmpty()
    }

    private fun DashboardEntryEntity.toDashboardEntryEntry(): DashboardEntry {
        return DashboardEntry(
            uid = uid,
            type = DashboardEntry.Type.valueOf(type),
            serverId = serverId,
            isVisible = isVisible,
            priority = priority
        )
    }

    private fun DashboardEntry.toDashboardEntryEntity(): DashboardEntryEntity {
        return DashboardEntryEntity(
            uid = uid,
            type = type.name,
            serverId = serverId,
            isVisible = isVisible,
            priority = priority
        )
    }
}
