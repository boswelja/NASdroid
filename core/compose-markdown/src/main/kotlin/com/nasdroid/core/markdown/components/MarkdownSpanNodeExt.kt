package com.nasdroid.core.markdown.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
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
    textStyles: TextStyles,
    imageSize: TextUnitSize,
): TextWithContent {
    val content = mutableMapOf<String, InlineTextContent>()
    val text = buildAnnotatedString {
        this@buildTextWithContent.forEach { node ->
            if (node is MarkdownImage) {
                content[node.imageUrl] = InlineTextContent(
                    // TODO auto-size the content - https://issuetracker.google.com/issues/294110693
                    placeholder = Placeholder(imageSize.width, imageSize.height, PlaceholderVerticalAlign.TextBottom)
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
            append(node.toAnnotatedString(textStyles))
        }
    }
    return TextWithContent(text, content)
}

/**
 * Describes the size of an element rendered inline with text.
 *
 * @property width The width of the element.
 * @property height THe height of the element.
 */
data class TextUnitSize(
    val width: TextUnit,
    val height: TextUnit,
)

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
    textStyles: TextStyles,
): AnnotatedString {
    return when (this) {
        is MarkdownCodeSpan -> AnnotatedString(
            text = text,
            spanStyle = textStyles.code(textStyles.textStyle).toSpanStyle()
        )
        is MarkdownImage -> buildAnnotatedString {
            appendInlineContent(imageUrl, contentDescription)
        }
        is MarkdownLink -> buildAnnotatedString {
            withAnnotation(UrlAnnotation(url)) {
                withStyle(textStyles.link(textStyles.textStyle).toSpanStyle()) {
                    displayText.forEach {
                        append(it.toAnnotatedString(textStyles))
                    }
                }
            }
        }
        is MarkdownText -> AnnotatedString(
            text = this.text,
            spanStyle = textStyles.textStyle
                .maybeLet(isBold, textStyles.bold)
                .maybeLet(isItalics, textStyles.italics)
                .maybeLet(isStrikethrough, textStyles.strikethrough)
                .toSpanStyle()
        )
        MarkdownWhitespace -> AnnotatedString(" ", textStyles.textStyle.toSpanStyle())
        MarkdownEol -> AnnotatedString("\n", textStyles.textStyle.toSpanStyle())
    }
}

internal inline fun <T> T.maybeLet(condition: Boolean, block: (T) -> T): T {
    return this.let {
        if (condition) block(it) else it
    }
}
