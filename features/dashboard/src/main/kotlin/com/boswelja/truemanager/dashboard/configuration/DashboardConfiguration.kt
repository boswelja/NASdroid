package com.boswelja.truemanager.dashboard.configuration

import kotlinx.coroutines.flow.Flow

/**
 * Allows retrieving and managing dashboard configurations for servers.
 */
interface DashboardConfiguration {

    /**
     * Get a list of entries that are visible for the specified server.
     */
    fun getVisibleEntries(serverId: String): Flow<List<DashboardEntry>>

    /**
     * Moves the entry with the given ID for the specified server up or down in the priority list.
     */
    suspend fun reorderEntry(serverId: String, entryId: String, newPriority: Int)

    /**
     * Adds new [DashboardEntry]s to the store.
     */
    suspend fun insertEntries(entries: List<DashboardEntry>)

    /**
     * Sets whether the specified entry is visible.
     */
    suspend fun setEntryVisible(serverId: String, entryId: String, isVisible: Boolean)
}

/**
 * Describes an entry on the dashboard.
 *
 * @property id The ID of the entry, unique to this server.
 * @property serverId The unique ID of the server this entry belongs to.
 * @property isVisible Whether this entry is visible on the dashboard.
 * @property priority THe priority of this entry. A lower number is displayed higher in the list.
 */
data class DashboardEntry(
    val id: String,
    val serverId: String,
    val isVisible: Boolean,
    val priority: Int
)
