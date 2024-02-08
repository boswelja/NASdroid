package com.nasdroid.core.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.generator.MarkdownCodeBlock

/**
 * Displays a [MarkdownCodeBlock]. A code block is a visually distinct section of text, usually used
 * to show code.
 */
@Composable
fun MarkdownCodeBlock(
    codeBlock: MarkdownCodeBlock,
    textStyle: TextStyle,
    background: Color,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .background(background, shape)
            .then(modifier)
    ) {
        BasicText(
            text = codeBlock.code,
            style = textStyle,
            modifier = Modifier.padding(8.dp)
        )
    }
}
