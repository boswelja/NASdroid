package com.boswelja.truemanager.dashboard.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.ui.overview.cards.CpuCard
import com.boswelja.truemanager.dashboard.ui.overview.cards.MemoryCard
import com.boswelja.truemanager.dashboard.ui.overview.cards.NetworkCard
import com.boswelja.truemanager.dashboard.ui.overview.cards.SystemInformationCard
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
    if (data != null) {
        val cardModifier = Modifier.fillMaxWidth()
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data!!) {
                when (it) {
                    is DashboardData.CpuData ->
                        CpuCard(
                            data = it,
                            modifier = cardModifier,
                        )
                    is DashboardData.MemoryData ->
                        MemoryCard(
                            data = it,
                            modifier = cardModifier,
                        )
                    is DashboardData.NetworkUsageData ->
                        NetworkCard(
                            data = it,
                            modifier = cardModifier,
                        )
                    is DashboardData.SystemInformationData ->
                        SystemInformationCard(
                            data = it,
                            modifier = cardModifier,
                        )
                }
            }
        }
    }
}
