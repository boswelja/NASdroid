package com.nasdroid.core.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.generator.MarkdownBlockQuote
import com.nasdroid.core.markdown.generator.MarkdownCodeBlock
import com.nasdroid.core.markdown.generator.MarkdownEol
import com.nasdroid.core.markdown.generator.MarkdownHeading
import com.nasdroid.core.markdown.generator.MarkdownHtmlBlock
import com.nasdroid.core.markdown.generator.MarkdownNode
import com.nasdroid.core.markdown.generator.MarkdownNodeGenerator
import com.nasdroid.core.markdown.generator.MarkdownOrderedList
import com.nasdroid.core.markdown.generator.MarkdownParagraph
import com.nasdroid.core.markdown.generator.MarkdownRule
import com.nasdroid.core.markdown.generator.MarkdownTable
import com.nasdroid.core.markdown.generator.MarkdownUnorderedList
import com.nasdroid.core.markdown.generator.MarkdownWhitespace
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    sectionSpacing: Dp = MaterialTheme.typography.bodyMedium.fontSize.toDp()
) {
    val parsedMarkdownNodes = remember(markdown) {
        val flavor = GFMFlavourDescriptor()
        val tree = MarkdownParser(flavor).buildMarkdownTreeFromString(markdown)
        MarkdownNodeGenerator(markdown, tree).generateNodes()
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(sectionSpacing)
    ) {
        parsedMarkdownNodes.forEach {
            MarkdownNode(node = it)
        }
    }
}

@Composable
internal fun TextUnit.toDp(): Dp {
    return with(LocalDensity.current) {
        this@toDp.toPx().toDp()
    }
}

@Composable
internal fun MarkdownNode(
    node: MarkdownNode,
    modifier: Modifier = Modifier
) {
    when (node) {
        is MarkdownBlockQuote -> MarkdownBlockQuote(blockQuote = node)
        is MarkdownCodeBlock -> MarkdownCodeBlock(codeBlock = node)
        is MarkdownHeading -> MarkdownHeading(node, modifier)
        is MarkdownOrderedList -> MarkdownOrderedList(node, modifier)
        is MarkdownParagraph -> MarkdownParagraph(
            paragraph = node,
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )
        MarkdownRule -> HorizontalDivider()
        is MarkdownTable -> TODO()
        is MarkdownHtmlBlock -> MarkdownHtmlBlock(node, modifier)
        is MarkdownUnorderedList -> MarkdownUnorderedList(node, modifier)
        MarkdownEol,
        MarkdownWhitespace -> { /* no-op */ }
    }
}

@Composable
internal fun MarkdownHtmlBlock(
    htmlBlock: MarkdownHtmlBlock,
    modifier: Modifier = Modifier
) {
    Text(htmlBlock.text, modifier = modifier)
}

@Composable
internal fun MarkdownOrderedList(
    list: MarkdownOrderedList,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(modifier) {
        list.listItems.forEachIndexed { index, markdownParagraph ->
            val annotatedString = remember(index, markdownParagraph) {
                buildAnnotatedString {
                    append("\t${index + 1}. ")
                    markdownParagraph.children.forEach { append(it.toAnnotatedString(textStyle)) }
                }
            }
            Text(annotatedString)
        }
    }
}

@Composable
internal fun MarkdownUnorderedList(
    list: MarkdownUnorderedList,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(modifier) {
        list.listItems.forEachIndexed { index, markdownParagraph ->
            val annotatedString = remember(index, markdownParagraph) {
                buildAnnotatedString {
                    append("\t\u2022 ")
                    markdownParagraph.children.forEach { append(it.toAnnotatedString(textStyle)) }
                }
            }
            Text(annotatedString)
        }
    }
}

@Composable
internal fun MarkdownCodeBlock(
    codeBlock: MarkdownCodeBlock,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .background(Color.Gray, MaterialTheme.shapes.medium)
            .then(modifier)
    ) {
        Text(
            text = codeBlock.code,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
internal fun MarkdownBlockQuote(
    blockQuote: MarkdownBlockQuote,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .background(Color.Gray, MaterialTheme.shapes.medium)
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

@Composable
internal fun MarkdownHeading(
    heading: MarkdownHeading,
    modifier: Modifier = Modifier
) {
    val typeStyle = when (heading.size) {
        MarkdownHeading.Size.Headline1 -> MaterialTheme.typography.displaySmall
        MarkdownHeading.Size.Headline2 -> MaterialTheme.typography.headlineLarge
        MarkdownHeading.Size.Headline3 -> MaterialTheme.typography.headlineMedium
        MarkdownHeading.Size.Headline4 -> MaterialTheme.typography.headlineSmall
        MarkdownHeading.Size.Headline5 -> MaterialTheme.typography.titleLarge
        MarkdownHeading.Size.Headline6 -> MaterialTheme.typography.titleMedium
    }
    val annotatedString = remember(heading) {
        buildAnnotatedString {
            heading.children.forEach {
                append(it.toAnnotatedString(typeStyle))
            }
        }
    }
    Text(
        text = annotatedString,
        modifier = modifier,
    )
}

@Preview(showBackground = true, heightDp = 1800)
@Composable
fun MarkdownTextPreview() {
    MarkdownText("""
        # H1        
        H1
        ==
        ## H2
        H2
        --
        ### H3
        #### H4
        ##### H5
        ###### H6

        One line

        Two
        lines

        _italics_
        *italics*

        __bold__
        **bold**

        ~~strikethrough~~

        [anilist link](https://anilist.co/)
        https://anilist.co/
        <https://anilist.co/>

        ![anilist icon](https://anilist.co/img/icons/icon.svg)
        ![image](https://picsum.photos/400)
        ![image](https://picsum.photos/300)
        ![image](https://picsum.photos/200)

        ---
        ***

        > block quote
        
        >> nested block quote

        - Bulleted
        - List

        * Bulleted
        * List

        + Bulleted
        + List

        + Nested
          + List

        1. Numbered
        2. List

        6. Numbered
        9. List

        * Mixed
          1. List
            + Containing
          2. Different
        * Types

        `inline code`

            block
            code

        ```
        block
        code
        ```
    """.trimIndent(),
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()))
}