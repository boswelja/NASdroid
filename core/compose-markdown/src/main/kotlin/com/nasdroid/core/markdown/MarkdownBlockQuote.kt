package com.nasdroid.core.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.generator.MarkdownBlockQuote

/**
 * Displays a [MarkdownBlockQuote]. A block quote is a visually distinct section in a document,
 * usually used to reference external sources.
 */
@Composable
fun MarkdownBlockQuote(
    blockQuote: MarkdownBlockQuote,
    backgroundColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape)
            .then(modifier)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.typography.bodyMedium.fontSize.toDp())
        ) {
            blockQuote.children.forEach {
                MarkdownNode(node = it)
            }
        }
    }
}
