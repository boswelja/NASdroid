package com.nasdroid.core.markdown.components

import androidx.compose.ui.text.TextStyle

/**
 * Describes possible styles for text within a span node.
 *
 * @property textStyle The standard text style.
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
data class TextStyles(
    val textStyle: TextStyle,
    val bold: (TextStyle) -> TextStyle,
    val italics: (TextStyle) -> TextStyle,
    val strikethrough: (TextStyle) -> TextStyle,
    val link: (TextStyle) -> TextStyle,
    val code: (TextStyle) -> TextStyle,
)