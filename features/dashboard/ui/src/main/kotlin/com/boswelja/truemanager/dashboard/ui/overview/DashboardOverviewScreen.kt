package com.boswelja.truemanager.dashboard.ui.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.truemanager.dashboard.logic.dataloading.DashboardData
import com.boswelja.truemanager.dashboard.ui.R
import com.boswelja.truemanager.dashboard.ui.overview.cards.CpuOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.MemoryOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.NetworkOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.SystemInformationOverview
import com.boswelja.truemanager.dashboard.ui.overview.cards.common.DashboardCard
import com.boswelja.truemanager.dashboard.ui.overview.cards.common.DashboardCardEditControls
import org.koin.androidx.compose.getViewModel

/**
 * The Dashboard Overview screen. This displays a list of user-configurable glanceable items for the
 * system.
 */
@Composable
fun DashboardOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: OverviewViewModel = getViewModel()
) {
    val items by viewModel.dashboardData.collectAsState()
    val editingItems by viewModel.editingList.collectAsState()
    val isEditing by remember(editingItems) {
        derivedStateOf { editingItems != null }
    }
    items?.let {
        DashboardOverviewList(
            items = editingItems ?: it,
            isEditing = isEditing,
            contentPadding = contentPadding,
            onMoveItem = viewModel::moveDashboardEntry,
            onStartEditing = viewModel::startEditing,
            modifier = modifier
        )
    }
}

/**
 * Displays the given dashboard items, with toggleable editing support.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardOverviewList(
    items: List<DashboardData>,
    isEditing: Boolean,
    onMoveItem: (from: Int, to: Int) -> Unit,
    onStartEditing: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = items,
            key = { _, data -> data.uid }
        ) { index, data ->
            OverviewCard(
                data = data,
                cardEditControls = DashboardCardEditControls(
                    isEditing = isEditing,
                    canMoveUp = index > 0,
                    canMoveDown = index < items.size - 1,
                    onMoveUp = {
                        onMoveItem(index, index - 1)
                    },
                    onMoveDown = {
                        onMoveItem(index, index + 1)
                    }
                ),
                onLongClick = onStartEditing,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
            )
        }
    }
}

/**
 * A convenience Composable that takes a generic [DashboardData] and renders the appropriate card
 * with details.
 */
@Composable
fun OverviewCard(
    data: DashboardData,
    cardEditControls: DashboardCardEditControls,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
) {
    when (data) {
        is DashboardData.CpuData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.cpu_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
                modifier = modifier
            ) {
                CpuOverview(data = data)
            }
        }
        is DashboardData.MemoryData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.memory_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
                modifier = modifier
            ) {
                MemoryOverview(data = data)
            }
        }
        is DashboardData.NetworkUsageData -> {
            DashboardCard(
                title = { Text(stringResource(R.string.network_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
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
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
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
