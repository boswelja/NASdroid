package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

class GetUpgradeDetails(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    suspend operator fun invoke(releaseName: String, targetVersion: String? = null): UpgradeDetails {
        val upgradeSummary = chartReleaseV2Api.getUpgradeSummary(releaseName, targetVersion)
        return UpgradeDetails(
            containerImagesToUpdate = upgradeSummary.containerImagesToUpdate.map { it.key },
            changelog = upgradeSummary.changelog,
            availableVersions = upgradeSummary.availableVersions.map { it.humanVersion }, // TODO include non-human versions for API calls
            targetVersion = upgradeSummary.upgradeHumanVersion
        )
    }
}

data class UpgradeDetails(
    val containerImagesToUpdate: List<String>,
    val changelog: String?,
    val availableVersions: List<String>,
    val targetVersion: String,
)
