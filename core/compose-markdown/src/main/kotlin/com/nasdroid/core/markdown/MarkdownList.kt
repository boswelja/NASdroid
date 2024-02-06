package com.nasdroid.core.markdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.nasdroid.core.markdown.generator.MarkdownOrderedList
import com.nasdroid.core.markdown.generator.MarkdownUnorderedList

@Composable
fun MarkdownOrderedList(
    list: MarkdownOrderedList,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        list.listItems.forEachIndexed { index, markdownParagraph ->
            val (annotatedString, inlineContent) = remember(index, markdownParagraph) {
                val textWithContent = markdownParagraph.children.buildTextWithContent(textStyle)
                val listItemText = buildAnnotatedString {
                    withStyle(textStyle.toSpanStyle()) {
                        append("\t${index + 1}. ")
                    }
                    append(textWithContent.text)
                }
                Pair(listItemText, textWithContent.content)
            }
            BasicText(
                text = annotatedString,
                inlineContent = inlineContent
            )
        }
    }
}

@Composable
fun MarkdownUnorderedList(
    list: MarkdownUnorderedList,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        list.listItems.forEach { markdownParagraph ->
            val (annotatedString, inlineContent) = remember(markdownParagraph) {
                val textWithContent = markdownParagraph.children.buildTextWithContent(textStyle)
                val listItemText = buildAnnotatedString {
                    withStyle(textStyle.toSpanStyle()) {
                        append("\t\u2022 ")
                    }
                    append(textWithContent.text)
                }
                Pair(listItemText, textWithContent.content)
            }
            BasicText(
                text = annotatedString,
                inlineContent = inlineContent
            )
        }
    }
}
