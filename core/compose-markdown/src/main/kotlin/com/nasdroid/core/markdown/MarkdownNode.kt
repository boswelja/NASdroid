package com.nasdroid.core.markdown

sealed interface MarkdownNode

data class MarkdownParagraph(
    val children: List<MarkdownSpanNode>
) : MarkdownNode

data class MarkdownBlockQuote(
    val children: List<MarkdownNode>
) : MarkdownNode

data object MarkdownRule : MarkdownNode

data object MarkdownWhitespace : MarkdownSpanNode

data object MarkdownEol: MarkdownSpanNode

data class MarkdownCodeBlock(
    val code: String,
    val language: String?
): MarkdownNode

data class MarkdownHeader(
    val children: List<MarkdownSpanNode>,
    val typeToken: TypographyToken,
) : MarkdownNode

sealed interface MarkdownSpanNode : MarkdownNode

data class MarkdownImage(
    val imageUrl: String,
    val contentDescription: String,
    val titleText: String?,
) : MarkdownSpanNode

data class MarkdownLink(
    val displayText: List<MarkdownText>,
    val titleText: String?,
    val url: String,
) : MarkdownSpanNode

data class MarkdownText(
    val text: String,
    val isBold: Boolean,
    val isItalics: Boolean,
    val isStrikethrough: Boolean,
) : MarkdownSpanNode

data class MarkdownCodeSpan(
    val text: String,
): MarkdownSpanNode
