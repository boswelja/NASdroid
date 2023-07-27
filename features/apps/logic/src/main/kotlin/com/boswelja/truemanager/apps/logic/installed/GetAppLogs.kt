package com.boswelja.truemanager.apps.logic.installed

import com.boswelja.truemanager.core.api.v2.chart.release.ChartReleaseV2Api
import com.boswelja.truemanager.core.api.v2.chart.release.PodLogsOptions

/**
 * Gets logs for an app instance. See [invoke] for details.
 */
class GetAppLogs(
    private val chartReleaseV2Api: ChartReleaseV2Api
) {

    /**
     * Gets the log output of a specified container for an app.
     *
     * @param releaseName The name of the app whose logs we are interested in.
     * @param logOptions The [LogOptions] to configure log retrieval.
     *
     * @return A List of String representing log lines.
     */
    suspend operator fun invoke(releaseName: String, logOptions: SelectedLogOptions): List<String> {
        return chartReleaseV2Api.getPodLogs(
            releaseName = releaseName,
            podLogsOptions = PodLogsOptions(
                podName = logOptions.podName,
                containerName = logOptions.containerName,
                tailLines = logOptions.maxLines
            )
        )
    }
}

/**
 * Describes options for the desired log.
 *
 * @property podName The name of the pod that hosts the app container we are interested in.
 * @property containerName The name of the app container running inside the pod.
 * @property maxLines The maximum number of lines to load for the log. For example, if this is set
 * to 500, the returned log will show the _last 500 lines_.
 */
data class SelectedLogOptions(
    val podName: String,
    val containerName: String,
    val maxLines: Long
)
