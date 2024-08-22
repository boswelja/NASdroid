package com.nasdroid.apps.ui.discover.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nasdroid.apps.logic.discover.AvailableAppDetails
import com.nasdroid.apps.ui.R
import com.nasdroid.design.MaterialThemeExt

@Composable
internal fun HelmChartInfo(
    chartInfo: AvailableAppDetails.ChartDetails,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = stringResource(R.string.discover_details_helm_chart_info_title),
            style = MaterialThemeExt.typography.titleMedium
        )
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_catalog_label),
            content = chartInfo.catalog
        )
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_train_label),
            content = chartInfo.train
        )
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_chart_version_label),
            content = chartInfo.chartVersion
        )
        AvailableAppListItem(
            label = stringResource(R.string.discover_details_maintainer_label),
            content = "${chartInfo.maintainers.first().name} (${chartInfo.maintainers.first().email})"
        )
    }
}
