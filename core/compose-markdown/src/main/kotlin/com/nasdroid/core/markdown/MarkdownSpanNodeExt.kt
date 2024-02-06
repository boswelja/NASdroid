package com.nasdroid.core.markdown

import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import com.nasdroid.core.markdown.generator.MarkdownCodeSpan
import com.nasdroid.core.markdown.generator.MarkdownEol
import com.nasdroid.core.markdown.generator.MarkdownImage
import com.nasdroid.core.markdown.generator.MarkdownLink
import com.nasdroid.core.markdown.generator.MarkdownSpanNode
import com.nasdroid.core.markdown.generator.MarkdownText
import com.nasdroid.core.markdown.generator.MarkdownWhitespace

@OptIn(ExperimentalTextApi::class)
fun MarkdownSpanNode.toAnnotatedString(
    textStyle: TextStyle,
    linkStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline),
    codeStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline)
): AnnotatedString {
    return when (this) {
        is MarkdownCodeSpan -> AnnotatedString(
            text = text,
            spanStyle = codeStyle.toSpanStyle()
        )
        is MarkdownImage -> buildAnnotatedString {
            appendInlineContent(imageUrl, contentDescription)
        }
        is MarkdownLink -> buildAnnotatedString {
            withAnnotation(UrlAnnotation(url)) {
                withStyle(linkStyle.toSpanStyle()) {
                    displayText.forEach {
                        append(it.toAnnotatedString(textStyle))
                    }
                }
            }
        }
        is MarkdownText -> AnnotatedString(
            text = this.text,
            spanStyle = textStyle
                .copy(
                    fontWeight = if (isBold) {
                        FontWeight.Bold
                    } else {
                        textStyle.fontWeight
                    },
                    fontStyle = if (isItalics) {
                        FontStyle.Italic
                    } else {
                        textStyle.fontStyle
                    },
                    textDecoration = if (isStrikethrough) {
                        TextDecoration.LineThrough
                    } else {
                        textStyle.textDecoration
                    }
                )
                .toSpanStyle()
        )
        MarkdownWhitespace -> AnnotatedString(" ", textStyle.toSpanStyle())
        MarkdownEol -> AnnotatedString("\n", textStyle.toSpanStyle())
    }
}
