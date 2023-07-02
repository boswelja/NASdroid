package com.boswelja.truemanager.dashboard.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.logic.DashboardData
import com.boswelja.truemanager.dashboard.ui.R
import com.boswelja.truemanager.dashboard.ui.overview.cards.CpuOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.MemoryOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.NetworkOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.SystemInformationOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.common.DashboardCard
import org.koin.androidx.compose.getViewModel

/**
 * The Dashboard Overview screen. This displays a list of user-configurable glanceable items for the
 * system.
 */
@Composable
fun OverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: OverviewViewModel = getViewModel()
) {
    val data by viewModel.dashboardData.collectAsState()
    var isEditing by rememberSaveable { mutableStateOf(false) }
    if (data != null) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data!!) {
                OverviewCard(data = it, isEditing = isEditing, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun OverviewCard(
    data: DashboardData,
    isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    when (data) {
        is DashboardData.CpuData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.cpu_card_title)) },
                modifier = modifier
            ) {
                CpuOverview(data = data)
            }
        }
        is DashboardData.MemoryData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.memory_card_title)) },
                modifier = modifier
            ) {
                MemoryOverview(data = data)
            }
        }
        is DashboardData.NetworkUsageData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.network_card_title)) },
                modifier = modifier
            ) {
                NetworkOverview(
                    data = data,
                    modifier = modifier,
                )
            }
        }
        is DashboardData.SystemInformationData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.system_info_card_title)) },
                modifier = modifier
            ) {
                SystemInformationOverview(
                    data = data,
                    modifier = modifier,
                )
            }
        }
    }
}
