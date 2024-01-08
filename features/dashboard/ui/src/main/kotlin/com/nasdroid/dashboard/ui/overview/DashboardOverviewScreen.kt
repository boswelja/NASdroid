package com.nasdroid.dashboard.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.boswelja.menuprovider.MenuItem
import com.boswelja.menuprovider.ProvideMenuItems
import com.nasdroid.dashboard.logic.configuration.DashboardItem
import com.nasdroid.dashboard.ui.R
import com.nasdroid.dashboard.ui.overview.common.DashboardCard
import com.nasdroid.dashboard.ui.overview.common.DashboardCardEditControls
import com.nasdroid.dashboard.ui.overview.cpu.CpuOverview
import com.nasdroid.dashboard.ui.overview.memory.MemoryOverview
import com.nasdroid.dashboard.ui.overview.network.NetworkOverview
import com.nasdroid.dashboard.ui.overview.system.SystemInformationOverview
import org.koin.androidx.compose.koinViewModel

/**
 * The Dashboard Overview screen. This displays a list of user-configurable glanceable items for the
 * system.
 */
@Composable
fun DashboardOverviewScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: OverviewViewModel = koinViewModel()
) {
    val items by viewModel.dashboardData.collectAsState()
    val editingItems by viewModel.editingList.collectAsState()
    val isEditing by remember(editingItems) {
        derivedStateOf { editingItems != null }
    }
    Box(modifier) {
        items?.getOrNull()?.let { dashboardItems ->
            ProvideMenuItems(
                if (isEditing) {
                    MenuItem(
                        label = "Stop Editing",
                        imageVector = Icons.Default.EditOff,
                        onClick = viewModel::stopEditing,
                        isImportant = true,
                    )
                } else {
                    MenuItem(
                        label = "Edit",
                        imageVector = Icons.Default.Edit,
                        onClick = viewModel::startEditing,
                        isImportant = true,
                    )
                }
            )
            DashboardOverviewList(
                items = editingItems ?: dashboardItems,
                isEditing = isEditing,
                contentPadding = contentPadding,
                onMoveItem = viewModel::moveDashboardEntry,
                onStartEditing = viewModel::startEditing,
            )
        }
    }
}

/**
 * Displays the given dashboard items, with toggleable editing support.
 */
@Composable
fun DashboardOverviewList(
    items: List<DashboardItem>,
    isEditing: Boolean,
    onMoveItem: (from: Int, to: Int) -> Unit,
    onStartEditing: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(280.dp),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = items,
            key = { _, data -> data.id }
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
            )
        }
    }
}

/**
 * A convenience Composable that takes a generic [DashboardItem] and displays the appropriate card
 * with details.
 */
@Composable
fun OverviewCard(
    data: DashboardItem,
    cardEditControls: DashboardCardEditControls,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
) {
    when (data.type) {
        DashboardItem.Type.Cpu -> {
            DashboardCard(
                title = { Text(stringResource(R.string.cpu_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
            ) {
                CpuOverview(modifier = modifier)
            }
        }
        DashboardItem.Type.Memory -> {
            DashboardCard(
                title = { Text(stringResource(R.string.memory_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
            ) {
                MemoryOverview(modifier = modifier)
            }
        }
        DashboardItem.Type.Network -> {
            DashboardCard(
                title = { Text(stringResource(R.string.network_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
            ) {
                NetworkOverview(modifier = modifier)
            }
        }
        DashboardItem.Type.SystemInformation -> {
            DashboardCard(
                title = { Text(stringResource(R.string.system_info_card_title)) },
                onClick = onClick,
                onLongClick = onLongClick,
                cardEditControls = cardEditControls,
            ) {
                SystemInformationOverview(modifier = modifier)
            }
        }
    }
}
