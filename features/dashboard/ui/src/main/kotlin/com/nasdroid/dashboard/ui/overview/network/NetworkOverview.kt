package com.nasdroid.dashboard.ui.overview.network

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boswelja.capacity.Capacity.Companion.bytes
import com.boswelja.capacity.CapacityUnit
import com.nasdroid.dashboard.logic.dataloading.network.NetworkConfiguration
import com.nasdroid.dashboard.logic.dataloading.network.NetworkUsageData
import com.nasdroid.dashboard.ui.R
import com.nasdroid.dashboard.ui.overview.common.OverviewItemListItem
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.skeleton.skeleton
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import org.koin.androidx.compose.koinViewModel

/**
 * A Card displaying an overview of the system network status.
 */
@Composable
fun NetworkOverview(
    modifier: Modifier = Modifier,
    viewModel: NetworkOverviewViewModel = koinViewModel()
) {
    val configuration by viewModel.networkConfiguration.collectAsState()
    val utilisation by viewModel.networkUsageData.collectAsState()

    val error by remember(configuration, utilisation) {
        derivedStateOf { configuration?.exceptionOrNull() ?: utilisation?.exceptionOrNull() }
    }

    Box(
        Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        NetworkOverview(
            configuration = configuration?.getOrNull(),
            utilisation = utilisation?.getOrNull(),
            modifier = modifier
        )

        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialThemeExt.colorScheme.errorContainer,
                        shape = MaterialThemeExt.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Something went wrong",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialThemeExt.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
internal fun NetworkOverview(
    configuration: NetworkConfiguration?,
    utilisation: NetworkUsageData?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        configuration?.adapters?.forEachIndexed { index, adapterData ->
            val adapterUtilisation = remember(adapterData, utilisation) {
                utilisation?.adapterUtilisation?.firstOrNull { it.name == adapterData.name }
            }
            if (index > 0) {
                HorizontalDivider(
                    Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
            AdapterInfo(
                adapterConfig = adapterData,
                adapterUtilisation = adapterUtilisation,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun AdapterInfo(
    adapterConfig: NetworkConfiguration.NetworkAdapter?,
    adapterUtilisation: NetworkUsageData.AdapterUtilisation?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = adapterConfig?.name ?: "adapter",
            style = MaterialThemeExt.typography.titleMedium,
            modifier = Modifier.skeleton(adapterConfig == null)
        )
        OverviewItemListItem(
            labelContent = { Text(stringResource(R.string.network_address_label)) },
            content = {
                Text(adapterConfig?.address ?: "None")
            },
            modifier = Modifier.skeleton(adapterConfig == null)
        )
        if (adapterUtilisation != null) {
            val chartModel = remember(adapterUtilisation) {
                CartesianChartModel(
                    ColumnCartesianLayerModel(
                        listOf(
                            listOf(
                                ColumnCartesianLayerModel.Entry(
                                    x = adapterUtilisation.sentBits.bytes.toDouble(CapacityUnit.MEGABYTE),
                                    y = 0
                                ),
                                ColumnCartesianLayerModel.Entry(
                                    x = adapterUtilisation.receivedBits.bytes.toDouble(CapacityUnit.MEGABYTE),
                                    y = 1
                                )
                            )
                        )
                    )
                )
            }
            ProvideVicoTheme(rememberM3VicoTheme()) {
                val context = LocalContext.current
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberColumnCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(
                            title = stringResource(R.string.network_data_rate_unit),
                            titleComponent = rememberTextComponent(color = LocalContentColor.current)
                        ),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            valueFormatter = { _, value, _ ->
                                if (value == 0.0) {
                                    context.getString(R.string.network_outgoing_label)
                                } else {
                                    context.getString(R.string.network_incoming_label)
                                }
                            }
                        )
                    ),
                    model = chartModel,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NetworkOverviewPreview() {
    NetworkOverview(
        configuration = NetworkConfiguration(
            adapters = listOf(
                NetworkConfiguration.NetworkAdapter(
                    name = "adapter1",
                    address = "192.168.1.2"
                ),
                NetworkConfiguration.NetworkAdapter(
                    name = "adapter2",
                    address = "192.168.1.3"
                )
            )
        ),
        utilisation = NetworkUsageData(
            adapterUtilisation = listOf(
                NetworkUsageData.AdapterUtilisation(
                    name = "adapter1",
                    sentBits = 99999999,
                    receivedBits = 10000000
                ),
                NetworkUsageData.AdapterUtilisation(
                    name = "adapter2",
                    sentBits = 10000000,
                    receivedBits = 99999999
                )
            )
        ),
        modifier = Modifier.padding(16.dp)
    )
}
