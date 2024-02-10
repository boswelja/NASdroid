package com.nasdroid.core.markdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.nasdroid.core.markdown.MarkdownNode
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
    nodeSpacing: Dp,
    textStyles: TextStyles,
    headingStyles: HeadingStyles,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .background(backgroundColor, shape)
            .then(modifier)
    ) {
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(nodeSpacing)
        ) {
            blockQuote.children.forEach {
                MarkdownNode(
                    node = it,
                    textStyles = textStyles,
                    headingStyles = headingStyles
                )
            }
        }
    }
}
