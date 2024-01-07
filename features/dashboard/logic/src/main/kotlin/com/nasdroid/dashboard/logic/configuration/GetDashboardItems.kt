package com.nasdroid.dashboard.logic.configuration

import com.nasdroid.api.exception.HttpNotOkException
import com.nasdroid.api.v2.system.SystemV2Api
import com.nasdroid.data.configuration.DashboardConfiguration
import com.nasdroid.data.configuration.DashboardEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Retrieves a list of [DashboardItem]s that the user has chosen to show in their dashboard. See
 * [invoke] for details.
 */
class GetDashboardItems(
    private val dashboardConfiguration: DashboardConfiguration,
    private val systemV2Api: SystemV2Api,
) {
    /**
     * Flows a [Result] containing either a list of [DashboardItem]s to be displayed, or an error.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<List<DashboardItem>>> = flow { emit(systemV2Api.getHostId()) }
        .flatMapLatest { dashboardConfiguration.getVisibleEntries(it) }
        .mapLatest { dashboardEntries ->
            val newList = dashboardEntries.map { dashboardEntry ->
                DashboardItem(
                    id = dashboardEntry.uid,
                    type = when (dashboardEntry.type) {
                        DashboardEntry.Type.SYSTEM_INFORMATION -> DashboardItem.Type.SystemInformation
                        DashboardEntry.Type.CPU -> DashboardItem.Type.Cpu
                        DashboardEntry.Type.MEMORY -> DashboardItem.Type.Memory
                        DashboardEntry.Type.NETWORK -> DashboardItem.Type.Network
                    }
                )
            }
            Result.success(newList)
        }
        .catch {
            when (it) {
                is HttpNotOkException -> emit(Result.failure(it))
                else -> throw it
            }
        }
}

/**
 * Describes an item to be shown on the dashboard.
 *
 * @property id The unique ID used to reference the item.
 * @property type The dashboard item type.
 */
data class DashboardItem(
    val id: Long,
    val type: Type
) {
    /**
     * The type of item that should be shown on the dashboard.
     */
    enum class Type {
        SystemInformation,
        Cpu,
        Memory,
        Network
    }
}
