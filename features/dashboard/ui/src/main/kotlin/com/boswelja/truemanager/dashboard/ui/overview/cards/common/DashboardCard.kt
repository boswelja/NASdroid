package com.boswelja.truemanager.dashboard.ui.overview.cards.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A generic Card used to display content in the Dashboard overview.
 */
@Composable
fun DashboardCard(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(modifier) {
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
    }
}

@Preview
@Composable
fun DashboardCardPreview() {
    DashboardCard(
        title = { Text("Title") },
        modifier = Modifier.fillMaxWidth(),
        content = { Text("Content") }
    )
}

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
