package com.boswelja.truemanager.dashboard.logic.configuration

import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import com.boswelja.truemanager.data.configuration.DashboardConfiguration
import com.boswelja.truemanager.data.configuration.DashboardEntry

/**
 * Sets whether an entry on the dashboard is visible. See [invoke] for details.
 */
class SetDashboardEntryVisible(
    private val configuration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api
) {

    /**
     * Sets whether the entry with the given type should be displayed in the dashboard. Hidden
     * entries retain their ordering.
     */
    suspend operator fun invoke(position: Int, visible: Boolean) {
        val serverId = systemV2Api.getHostId()
        configuration.setEntryVisible(serverId, position, visible)
    }
}
