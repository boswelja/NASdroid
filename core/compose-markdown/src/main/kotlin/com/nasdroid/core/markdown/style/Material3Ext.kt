package com.nasdroid.core.markdown.style

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * Constructs a [TextStyles] using recommended defaults from your Material 3 theme.
 */
@Composable
fun m3TextStyles(
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    headline1: TextStyle = MaterialTheme.typography.displaySmall,
    headline2: TextStyle = MaterialTheme.typography.headlineLarge,
    headline3: TextStyle = MaterialTheme.typography.headlineMedium,
    headline4: TextStyle = MaterialTheme.typography.headlineSmall,
    headline5: TextStyle = MaterialTheme.typography.titleLarge,
    headline6: TextStyle = MaterialTheme.typography.titleMedium,
): TextStyles {
    return TextStyles(
        textStyle = textStyle,
        headline1 = headline1,
        headline2 = headline2,
        headline3 = headline3,
        headline4 = headline4,
        headline5 = headline5,
        headline6 = headline6,
    )
}

/**
 * Constructs a [TextStyleModifiers] using recommended defaults from your Material 3 theme.
 */
@Composable
fun m3TextStyleModifiers(
    linkColor: Color = MaterialTheme.colorScheme.primary
): TextStyleModifiers {
    return TextStyleModifiers(
        bold = { it.copy(fontWeight = FontWeight.Bold) },
        italics = { it.copy(fontStyle = FontStyle.Italic) },
        strikethrough = { it.copy(textDecoration = TextDecoration.LineThrough) },
        link = { it.copy(color = linkColor, textDecoration = TextDecoration.Underline) },
        code = { it.copy(fontFamily = FontFamily.Monospace) }
    )
}

@Composable
fun m3BlockQuoteStyle(): BlockQuoteStyle {
    return BlockQuoteStyle(
        background = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        innerPadding = PaddingValues(8.dp)
    )
}

@Composable
fun m3CodeBlockStyle(): CodeBlockStyle {
    return CodeBlockStyle(
        background = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        innerPadding = PaddingValues(8.dp)
    )
}
