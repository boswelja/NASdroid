package com.boswelja.truemanager.dashboard.logic

import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.DashboardEntry

/**
 * Moves an entry in the dashboard from one position to another. See [invoke] for details.
 */
class MoveDashboardEntry(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api
) {

    /**
     * Relocates the entry with the given type to a new position, based on the given priority.
     * Entries between the old and new value will be shifted up or down accordingly. For example:
     * ```
     * // Lets say we start with something like this, and we want to move CPU to the last item
     * 1 - CPU
     * 2 - Memory
     * 3 - Network
     * 4 - Storage
     * // The result would look like this
     * 1 - Memory
     * 2 - Network
     * 3 - Storage
     * 4 - CPU
     * ```
     */
    suspend operator fun invoke(entryType: DashboardEntry.Type, newPriority: Int) {
        val serverId = systemV2Api.getHostId()
        configuration.reorderEntry(serverId, entryType, newPriority)
    }
}
