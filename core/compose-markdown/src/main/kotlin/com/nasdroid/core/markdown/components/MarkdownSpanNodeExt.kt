package com.nasdroid.core.markdown.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.core.markdown.generator.MarkdownCodeSpan
import com.nasdroid.core.markdown.generator.MarkdownEol
import com.nasdroid.core.markdown.generator.MarkdownImage
import com.nasdroid.core.markdown.generator.MarkdownLink
import com.nasdroid.core.markdown.generator.MarkdownSpanNode
import com.nasdroid.core.markdown.generator.MarkdownText
import com.nasdroid.core.markdown.generator.MarkdownWhitespace

/**
 * Maps a list of [MarkdownSpanNode]s to a [TextWithContent] for use in a Text Composable.
 */
fun List<MarkdownSpanNode>.buildTextWithContent(
    textStyle: TextStyle,
    linkStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline),
    codeStyle: TextStyle = textStyle.copy(background = Color.Gray, fontFamily = FontFamily.Monospace),
): TextWithContent {
    val content = mutableMapOf<String, InlineTextContent>()
    val text = buildAnnotatedString {
        this@buildTextWithContent.forEach { node ->
            if (node is MarkdownImage) {
                content[node.imageUrl] = InlineTextContent(
                    // TODO auto-size the content - https://issuetracker.google.com/issues/294110693
                    placeholder = Placeholder(100.sp, 100.sp, PlaceholderVerticalAlign.TextBottom)
                ) {
                    val request = when {
                        node.imageUrl.endsWith("svg") -> ImageRequest.Builder(LocalContext.current)
                            .data(node.imageUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build()
                        node.imageUrl.endsWith("gif") -> ImageRequest.Builder(LocalContext.current)
                            .data(node.imageUrl)
                            .decoderFactory(ImageDecoderDecoder.Factory())
                            .crossfade(true)
                            .build()
                        else -> ImageRequest.Builder(LocalContext.current)
                            .data(node.imageUrl)
                            .crossfade(true)
                            .build()
                    }
                    AsyncImage(
                        model = request,
                        contentDescription = it,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            append(node.toAnnotatedString(textStyle, linkStyle, codeStyle))
        }
    }
    return TextWithContent(text, content)
}

/**
 * Describes an [AnnotatedString], along with a map describing inline content within the annotated
 * string.
 *
 * @property text The [AnnotatedString] to be displayed.
 * @property content The map containing [InlineTextContent]s to be displayed.
 */
data class TextWithContent(
    val text: AnnotatedString,
    val content: Map<String, InlineTextContent>
)

@OptIn(ExperimentalTextApi::class)
internal fun MarkdownSpanNode.toAnnotatedString(
    textStyle: TextStyle,
    linkStyle: TextStyle,
    codeStyle: TextStyle
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
                        append(it.toAnnotatedString(textStyle, linkStyle, codeStyle))
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
