package com.boswelja.truemanager.dashboard.configuration

import kotlinx.coroutines.flow.Flow

interface DashboardConfiguration {
    fun getVisibleEntries(): Flow<List<DashboardEntry>>

    suspend fun reorderEntry(entryId: String, newPriority: Int)

    suspend fun insertEntries(entries: List<DashboardEntry>)

    suspend fun setEntryVisible(entryId: String, isVisible: Boolean)
}

data class DashboardEntry(
    val id: String,
    val isVisible: Boolean,
    val priority: Int
)
