package com.nasdroid.dashboard.ui.overview.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasdroid.design.MaterialThemeExt

/**
 * A list item in a Dashboard card. This simply displays some labelled content, usually text.
 */
@Composable
fun OverviewItemListItem(
    labelContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialThemeExt.typography.labelLarge,
            content = labelContent
        )
        SelectionContainer {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialThemeExt.typography.bodyLarge,
                content = content
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewItemListItemPreview() {
    OverviewItemListItem(
        labelContent = { Text("Label") },
        content = { Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et.") },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
    )
}
