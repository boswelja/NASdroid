package com.nasdroid.core.markdown

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import com.nasdroid.core.markdown.generator.MarkdownParagraph

/**
 * Displays a [MarkdownParagraph]. A paragraph is a group of "spans". Spans are stylized sections of
 * text, but can also include inline images and links.
 */
@Composable
fun MarkdownParagraph(
    paragraph: MarkdownParagraph,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    linkStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline),
    codeStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline)
) {
    val (annotatedString, inlineContent) = remember(paragraph) {
        paragraph.children.buildTextWithContent(textStyle, linkStyle, codeStyle)
    }
    BasicText(
        text = annotatedString,
        modifier = modifier,
        inlineContent = inlineContent
    )
}
