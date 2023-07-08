package com.boswelja.truemanager.dashboard.ui.overview.cards.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.MoveUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A generic Card used to display content in the Dashboard overview.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardCard(
    title: @Composable () -> Unit,
    onClick: () -> Unit,
    cardEditControls: DashboardCardEditControls,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier.combinedClickable(
            onLongClick = onLongClick,
            onClick = onClick
        ),
    ) {
        Column(Modifier.padding(DashboardCardDefaults.ContentPadding)) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleLarge,
                content = title
            )
            Spacer(Modifier.height(DashboardCardDefaults.TitleContentSpacing))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = content
            )
        }
        // We animate the divider separately so it can fade in separate from the other content,
        // giving us a better "growing card" effect
        AnimatedVisibility(visible = cardEditControls.isEditing) {
            Divider()
        }
        AnimatedVisibility(
            visible = cardEditControls.isEditing,
            enter = expandVertically(expandFrom = Alignment.Bottom),
            exit = shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            DashboardCardOrderControls(
                editControls = cardEditControls,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardCardOrderControls(
    editControls: DashboardCardEditControls,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        PlainTooltipBox(
            tooltip = { Text(if (editControls.isVisible) "Hide card in dashboard" else "Show card in dashboard") }
        ) {
            IconButton(
                onClick = editControls.onVisibilityToggle,
                modifier = Modifier.tooltipTrigger()
            ) {
                Icon(
                    imageVector = if (editControls.isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (editControls.isVisible) "Hide card in dashboard" else "Show card in dashboard"
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        PlainTooltipBox(tooltip = { Text("Move up") }) {
            IconButton(
                onClick = editControls.onMoveUp,
                modifier = Modifier.tooltipTrigger(),
                enabled = editControls.canMoveUp
            ) {
                Icon(imageVector = Icons.Default.MoveUp, contentDescription = "Move up")
            }
        }
        PlainTooltipBox(tooltip = { Text("Move down") }) {
            IconButton(
                onClick = editControls.onMoveDown,
                modifier = Modifier.tooltipTrigger(),
                enabled = editControls.canMoveDown
            ) {
                Icon(imageVector = Icons.Default.MoveDown, contentDescription = "Move down")
            }
        }
    }
}

@Preview
@Composable
fun DashboardCardPreview() {
    var isEditing by remember { mutableStateOf(true) }
    var isVisible by remember { mutableStateOf(true) }
    var index by remember { mutableIntStateOf(0) }
    val imaginaryItemCount = 3

    MaterialTheme {
        DashboardCard(
            title = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            content = { Text("Content") },
            onClick = {
                isEditing = !isEditing
            },
            cardEditControls = DashboardCardEditControls(
                isEditing = isEditing,
                onVisibilityToggle = { isVisible = !isVisible },
                onMoveUp = { index-- },
                onMoveDown = { index++ },
                isVisible = isVisible,
                canMoveUp = index > 0,
                canMoveDown = index < imaginaryItemCount
            ),
        )
    }
}

@Stable
data class DashboardCardEditControls(
    val isEditing: Boolean,
    val isVisible: Boolean,
    val canMoveUp: Boolean,
    val canMoveDown: Boolean,
    val onVisibilityToggle: () -> Unit,
    val onMoveUp: () -> Unit,
    val onMoveDown: () -> Unit,
)

/**
 * Holds default values for [DashboardCard].
 */
object DashboardCardDefaults {
    /**
     * The default padding between the edge of the card and the card content.
     */
    val ContentPadding = 16.dp

    /**
     * The default spacing between the title of the card and the card content.
     */
    val TitleContentSpacing = 12.dp
}
