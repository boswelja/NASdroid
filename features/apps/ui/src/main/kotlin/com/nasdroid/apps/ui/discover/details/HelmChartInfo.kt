package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.design.MaterialThemeExt

@Composable
internal fun HelmChartInfo(
    chartInfo: AvailableAppDetails.ChartDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = "Helm Chart Info",
            style = MaterialThemeExt.typography.titleMedium
        )
        AvailableAppListItem(
            label = "Catalog",
            content = chartInfo.catalog
        )
        AvailableAppListItem(
            label = "Train",
            content = chartInfo.train
        )
        AvailableAppListItem(
            label = "Chart Version",
            content = chartInfo.chartVersion
        )
        AvailableAppListItem(
            label = "Maintainer",
            content = "${chartInfo.maintainers.first().name} (${chartInfo.maintainers.first().email})"
        )
    }
}