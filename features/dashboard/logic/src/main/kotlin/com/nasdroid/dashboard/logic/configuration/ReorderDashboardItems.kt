package com.nasdroid.dashboard.logic.configuration

/**
 * Moves an entry in the dashboard from one position to another. See [invoke] for details.
 */
class ReorderDashboardItems {

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
    operator fun invoke(items: List<DashboardItem>, fromPosition: Int, toPosition: Int): List<DashboardItem> {
        val workingList = items.toMutableList()
        val target = workingList.removeAt(fromPosition)
        workingList.add(toPosition, target)
        return workingList.toList()
    }
}
