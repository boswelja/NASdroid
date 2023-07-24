package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api

/**
 * Retrieve all pods and containers that logs can be exported from. See [invoke] for details.
 */
class GetLogOptions(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Gets all available log options for the given release name.
     */
    suspend operator fun invoke(releaseName: String): LogOptions {
        val availableOptions = chartReleaseV2Api.getPodLogChoices(releaseName)
        return LogOptions(
            podsAndCharts = availableOptions
        )
    }
}

/**
 * Options available for log retrieval.
 *
 * @property podsAndCharts A Map of pod names to a list of containers inside the pod.
 */
data class LogOptions(
    val podsAndCharts: Map<String, List<String>>
)
