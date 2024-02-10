package com.nasdroid.core.markdown.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
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
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues()
) {
    Box(
        modifier = Modifier
            .background(background, shape)
            .then(modifier)
    ) {
        BasicText(
            text = codeBlock.code,
            style = textStyle,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
