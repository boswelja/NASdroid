package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

class GetInstalledAppOverview(
    private val chartReleaseV2Api: ChartReleaseV2Api,
) {

    suspend operator fun invoke(appId: String): InstalledAppOverview {
        val release = chartReleaseV2Api.getChartRelease(appId)
        return InstalledAppOverview(
            iconUrl = release.chartMetadata.icon,
            title = release.name
        )
    }
}

data class InstalledAppOverview(
    val iconUrl: String,
    val title: String,
)
