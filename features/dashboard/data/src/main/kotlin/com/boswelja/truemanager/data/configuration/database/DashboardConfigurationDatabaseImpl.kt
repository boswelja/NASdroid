package com.boswelja.truemanager.data.configuration.database

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.DashboardEntry
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
        .getVisible(serverId)
        .mapLatest {
            it.map { entity ->
                DashboardEntry(
                    entity.uid,
                    DashboardEntry.Type.valueOf(entity.type),
                    entity.serverId,
                    entity.isVisible,
                    entity.priority
                )
            }
        }

    override suspend fun reorderEntry(serverId: String, fromPosition: Int, toPosition: Int) {
        val dao = database.getDashboardEntryDao()
        database.withTransaction {
            val operatingItem = dao.get(serverId, fromPosition)
            val itemsToReorder = dao.getLowerPriority(serverId, operatingItem.priority)
            // If the new priority is greater than the old priority, the item is moving *down* in
            // priority. Thus, everything "below" it needs to move "up".
            val increasePriorityForItems = operatingItem.priority < toPosition
            val newItems = itemsToReorder.map { entity ->
                entity.copy(
                    priority = if (increasePriorityForItems) entity.priority - 1 else entity.priority + 1
                )
            }
            dao.update(newItems + operatingItem.copy(priority = toPosition))
        }
    }

    override suspend fun insertEntries(entries: List<DashboardEntry>) {
        database.getDashboardEntryDao().add(
            entries.map {
                DashboardEntryEntity(it.uid, it.type.name, it.serverId, it.isVisible, it.priority)
            }
        )
    }

    override suspend fun hasAnyEntries(serverId: String): Boolean {
        return database.getDashboardEntryDao().getAll().first().isNotEmpty()
    }

    override suspend fun setEntryVisible(serverId: String, position: Int, isVisible: Boolean) {
        database.getDashboardEntryDao().update(serverId, position, isVisible)
    }
}
