package com.nasdroid.dashboard.logic.configuration

import com.nasdroid.api.v2.exception.HttpNotOkException
import com.nasdroid.api.websocket.system.SystemApi
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
    private val systemApi: SystemApi,
) {
    /**
     * Flows a [Result] containing either a list of [DashboardItem]s to be displayed, or an error.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Result<List<DashboardItem>>> = flow { emit(systemApi.hostId()) }
        .flatMapLatest { dashboardConfiguration.getVisibleEntries(it) }
        .mapLatest { dashboardEntries ->
            val newList = dashboardEntries.map { dashboardEntry ->
                when (dashboardEntry.type) {
                    DashboardEntry.Type.SystemInformation -> DashboardItem.SystemInformation(
                        id = dashboardEntry.uid,
                        subtype = DashboardItem.SystemInformation.Subtype.valueOf(dashboardEntry.sybtype!!),
                    )
                    DashboardEntry.Type.Cpu -> DashboardItem.Cpu(
                        id = dashboardEntry.uid,
                        subtype = DashboardItem.Cpu.Subtype.valueOf(dashboardEntry.sybtype!!)
                    )
                    DashboardEntry.Type.Memory -> DashboardItem.Memory(id = dashboardEntry.uid)
                    DashboardEntry.Type.Network -> DashboardItem.Network(
                        id = dashboardEntry.uid,
                        subtype = DashboardItem.Network.Subtype.valueOf(dashboardEntry.sybtype!!),
                        `interface` = dashboardEntry.extra!!
                    )
                    DashboardEntry.Type.Storage -> DashboardItem.Storage(
                        id = dashboardEntry.uid,
                        subtype = when (dashboardEntry.sybtype) {
                            "Pool" -> DashboardItem.Storage.Subtype.Pool(dashboardEntry.extra!!)
                            "PoolUsage" -> DashboardItem.Storage.Subtype.PoolUsage(dashboardEntry.extra!!)
                            "Storage" -> DashboardItem.Storage.Subtype.Storage
                            "DisksWithErrors" -> DashboardItem.Storage.Subtype.DisksWithErrors(dashboardEntry.extra!!)
                            "LastScanErrors" -> DashboardItem.Storage.Subtype.LastScanErrors(dashboardEntry.extra!!)
                            "PoolStatus" -> DashboardItem.Storage.Subtype.PoolStatus(dashboardEntry.extra!!)
                            else -> throw IllegalArgumentException("Unknown storage subtype ${dashboardEntry.sybtype}")
                        }
                    )
                    DashboardEntry.Type.BackupTasks -> DashboardItem.BackupTasks(id = dashboardEntry.uid)
                }
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

