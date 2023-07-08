package com.boswelja.truemanager.data.configuration

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
     * Get whether the server with the given ID has any entries associated at all.
     */
    suspend fun hasAnyEntries(serverId: String): Boolean

    /**
     * Moves the entry with the given ID for the specified server up or down in the priority list.
     */
    suspend fun reorderEntry(serverId: String, fromPosition: Int, toPosition: Int)

    /**
     * Adds new [DashboardEntry]s to the store.
     */
    suspend fun insertEntries(entries: List<DashboardEntry>)

    /**
     * Sets whether the specified entry is visible.
     */
    suspend fun setEntryVisible(serverId: String, position: Int, isVisible: Boolean)
}

/**
 * Describes an entry on the dashboard.
 *
 * @property type The type of dashboard entry.
 * @property serverId The unique ID of the server this entry belongs to.
 * @property isVisible Whether this entry is visible on the dashboard.
 * @property priority THe priority of this entry. A lower number is displayed higher in the list.
 */
data class DashboardEntry(
    val type: Type,
    val serverId: String,
    val isVisible: Boolean,
    val priority: Int
) {
    /**
     * All supported types of dashboard elements.
     */
    enum class Type {
        SYSTEM_INFORMATION,
        CPU,
        MEMORY,
        NETWORK,
    }
}
