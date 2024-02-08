package com.nasdroid.core.markdown

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.nasdroid.core.markdown.generator.MarkdownHeading

/**
 * Displays a [MarkdownHeading]. A heading is a higher emphasis paragraph, usually used to separate
 * a document into sections.
 */
@Composable
fun MarkdownHeading(
    heading: MarkdownHeading,
    headingStyles: HeadingStyles,
    modifier: Modifier = Modifier
) {
    val textStyle = when (heading.size) {
        MarkdownHeading.Size.Headline1 -> headingStyles.headline1
        MarkdownHeading.Size.Headline2 -> headingStyles.headline2
        MarkdownHeading.Size.Headline3 -> headingStyles.headline3
        MarkdownHeading.Size.Headline4 -> headingStyles.headline4
        MarkdownHeading.Size.Headline5 -> headingStyles.headline5
        MarkdownHeading.Size.Headline6 -> headingStyles.headline6
    }
    val (annotatedString, inlineContent) = remember(heading) {
        heading.children.buildTextWithContent(textStyle)
    }
    BasicText(
        text = annotatedString,
        modifier = modifier,
        inlineContent = inlineContent
    )
}

/**
 * Describes the styles available to all different Markdown headings.
 */
@Suppress("UndocumentedPublicProperty") // These are (hopefully) self-explanatory.
data class HeadingStyles(
    val headline1: TextStyle,
    val headline2: TextStyle,
    val headline3: TextStyle,
    val headline4: TextStyle,
    val headline5: TextStyle,
    val headline6: TextStyle,
)
