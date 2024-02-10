package com.nasdroid.core.markdown

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.nasdroid.core.markdown.components.HeadingStyles
import com.nasdroid.core.markdown.components.MarkdownBlockQuote
import com.nasdroid.core.markdown.components.MarkdownCodeBlock
import com.nasdroid.core.markdown.components.MarkdownHeading
import com.nasdroid.core.markdown.components.MarkdownHtmlBlock
import com.nasdroid.core.markdown.components.MarkdownOrderedList
import com.nasdroid.core.markdown.components.MarkdownParagraph
import com.nasdroid.core.markdown.components.MarkdownUnorderedList
import com.nasdroid.core.markdown.components.TextStyles
import com.nasdroid.core.markdown.generator.MarkdownBlockQuote
import com.nasdroid.core.markdown.generator.MarkdownCodeBlock
import com.nasdroid.core.markdown.generator.MarkdownHeading
import com.nasdroid.core.markdown.generator.MarkdownHtmlBlock
import com.nasdroid.core.markdown.generator.MarkdownNode
import com.nasdroid.core.markdown.generator.MarkdownNodeGenerator
import com.nasdroid.core.markdown.generator.MarkdownOrderedList
import com.nasdroid.core.markdown.generator.MarkdownParagraph
import com.nasdroid.core.markdown.generator.MarkdownRule
import com.nasdroid.core.markdown.generator.MarkdownTable
import com.nasdroid.core.markdown.generator.MarkdownUnorderedList
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

/**
 * Displays a Markdown document.
 */
@Composable
fun MarkdownDocument(
    markdown: String,
    modifier: Modifier = Modifier,
    sectionSpacing: Dp = MaterialTheme.typography.bodyLarge.fontSize.toDp()
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
            val primaryColor = MaterialTheme.colorScheme.primary
            MarkdownNode(
                node = it,
                textStyles = TextStyles(
                    textStyle = MaterialTheme.typography.bodyLarge,
                    bold = { it.copy(fontWeight = FontWeight.Bold) },
                    italics = { it.copy(fontStyle = FontStyle.Italic) },
                    strikethrough = { it.copy(textDecoration = TextDecoration.LineThrough) },
                    link = { it.copy(color = primaryColor, textDecoration = TextDecoration.Underline) },
                    code = { it.copy(fontFamily = FontFamily.Monospace) }
                ),
                headingStyles = HeadingStyles(
                    headline1 = MaterialTheme.typography.displaySmall,
                    headline2 = MaterialTheme.typography.headlineLarge,
                    headline3 = MaterialTheme.typography.headlineMedium,
                    headline4 = MaterialTheme.typography.headlineSmall,
                    headline5 = MaterialTheme.typography.titleLarge,
                    headline6 = MaterialTheme.typography.titleMedium,
                    bold = { it.copy(fontWeight = FontWeight.Bold) },
                    italics = { it.copy(fontStyle = FontStyle.Italic) },
                    strikethrough = { it.copy(textDecoration = TextDecoration.LineThrough) },
                    link = { it.copy(color = primaryColor, textDecoration = TextDecoration.Underline) },
                    code = { it.copy(fontFamily = FontFamily.Monospace) }
                )
            )
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
    textStyles: TextStyles,
    headingStyles: HeadingStyles,
    modifier: Modifier = Modifier
) {
    when (node) {
        is MarkdownBlockQuote -> MarkdownBlockQuote(
            blockQuote = node,
            backgroundColor = Color.Gray,
            nodeSpacing = textStyles.textStyle.fontSize.toDp(),
            shape = MaterialTheme.shapes.medium,
            textStyles = textStyles,
            headingStyles = headingStyles,
            innerPadding = PaddingValues(8.dp)
        )
        is MarkdownCodeBlock -> MarkdownCodeBlock(
            codeBlock = node,
            textStyle = textStyles.textStyle.copy(fontFamily = FontFamily.Monospace),
            background = Color.Gray,
            shape = MaterialTheme.shapes.medium,
            innerPadding = PaddingValues(8.dp)
        )
        is MarkdownHeading -> MarkdownHeading(
            heading = node,
            modifier = modifier,
            headingStyles = headingStyles,
        )
        is MarkdownOrderedList -> MarkdownOrderedList(
            list = node,
            textStyles = textStyles,
            headingStyles = headingStyles,
            modifier = modifier
        )
        is MarkdownParagraph -> MarkdownParagraph(
            paragraph = node,
            textStyles = textStyles,
            modifier = modifier
        )
        MarkdownRule -> HorizontalDivider()
        is MarkdownTable -> TODO()
        is MarkdownHtmlBlock -> MarkdownHtmlBlock(
            htmlBlock = node,
            textStyle = textStyles.textStyle,
            modifier = modifier
        )
        is MarkdownUnorderedList -> MarkdownUnorderedList(
            list = node,
            textStyles = textStyles,
            headingStyles = headingStyles,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, heightDp = 1900)
@Composable
fun MarkdownTextPreview() {
    MarkdownDocument("""
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
        ![image](https://picsum.photos/50)

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
            .verticalScroll(rememberScrollState())
            .padding(16.dp))
}