package com.nasdroid.core.markdown

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.nasdroid.core.markdown.generator.MarkdownImage
import com.nasdroid.core.markdown.generator.MarkdownParagraph

@Composable
fun MarkdownParagraph(
    paragraph: MarkdownParagraph,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    linkStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline),
    codeStyle: TextStyle = textStyle.copy(color = Color.Blue, textDecoration = TextDecoration.Underline)
) {
    val (annotatedString, inlineContent) = remember(paragraph) {
        val content = mutableMapOf<String, InlineTextContent>()
        val text = buildAnnotatedString {
            paragraph.children.forEach { image ->
                if (image is MarkdownImage) {
                    content[image.imageUrl] = InlineTextContent(
                        // TODO auto-size the content
                        placeholder = Placeholder(100.sp, 100.sp, PlaceholderVerticalAlign.TextBottom)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(image.imageUrl)
                                .decoderFactory(SvgDecoder.Factory())
                                .crossfade(true)
                                .build(),
                            contentDescription = it
                        )
                    }
                }
                append(image.toAnnotatedString(textStyle, linkStyle, codeStyle))
            }
        }
        Pair(text, content)
    }
    BasicText(
        text = annotatedString,
        modifier = modifier,
        inlineContent = inlineContent
    )
}
