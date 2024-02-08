package com.nasdroid.core.markdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.generator.MarkdownOrderedList
import com.nasdroid.core.markdown.generator.MarkdownUnorderedList

@Composable
fun MarkdownOrderedList(
    list: MarkdownOrderedList,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        list.listItems.forEachIndexed { index, markdownNode ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Top
            ) {
                BasicText(
                    text = "\t${index + 1}.",
                    style = textStyle
                )
                MarkdownNode(node = markdownNode)
            }
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
        list.listItems.forEach { markdownNode ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BasicText(
                    text = "\t\u2022",
                    style = textStyle
                )
                MarkdownNode(node = markdownNode)
            }
        }
    }
}
