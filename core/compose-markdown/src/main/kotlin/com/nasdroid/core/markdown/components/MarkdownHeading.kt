package com.nasdroid.core.markdown.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
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
    val (annotatedString, inlineContent) = remember(heading, headingStyles) {
        val textStyles = TextStyles(
            textStyle = when (heading.size) {
                MarkdownHeading.Size.Headline1 -> headingStyles.headline1
                MarkdownHeading.Size.Headline2 -> headingStyles.headline2
                MarkdownHeading.Size.Headline3 -> headingStyles.headline3
                MarkdownHeading.Size.Headline4 -> headingStyles.headline4
                MarkdownHeading.Size.Headline5 -> headingStyles.headline5
                MarkdownHeading.Size.Headline6 -> headingStyles.headline6
            },
            bold = headingStyles.bold,
            italics = headingStyles.italics,
            strikethrough = headingStyles.strikethrough,
            link = headingStyles.link,
            code = headingStyles.code
        )
        heading.children.buildTextWithContent(textStyles, TextUnitSize(100.sp, 100.sp))
    }
    BasicText(
        text = annotatedString,
        modifier = modifier,
        inlineContent = inlineContent
    )
}

/**
 * Describes the styles available to all different Markdown headings.
 *
 * @property bold A function that produces a [TextStyle] for bold text. It is recommended to modify
 * the provided TextStyle, which may already be stylized for other formatting.
 * @property italics A function that produces a [TextStyle] for italicized text. It is recommended
 * to modify the provided TextStyle, which may already be stylized for other formatting.
 * @property strikethrough A function that produces a [TextStyle] for strikethrough text. It is
 * recommended to modify the provided TextStyle, which may already be stylized for other formatting.
 * @property link A function that produces a [TextStyle] for clickable link text. It is recommended
 * to modify the provided TextStyle, which may already be stylized for other formatting.
 * @property code A function that produces a [TextStyle] for inline code text. It is recommended to
 * modify the provided TextStyle, which may already be stylized for other formatting.
 */
@Suppress("UndocumentedPublicProperty") // These are (hopefully) self-explanatory.
data class HeadingStyles(
    val headline1: TextStyle,
    val headline2: TextStyle,
    val headline3: TextStyle,
    val headline4: TextStyle,
    val headline5: TextStyle,
    val headline6: TextStyle,
    val bold: (TextStyle) -> TextStyle,
    val italics: (TextStyle) -> TextStyle,
    val strikethrough: (TextStyle) -> TextStyle,
    val link: (TextStyle) -> TextStyle,
    val code: (TextStyle) -> TextStyle,
)
