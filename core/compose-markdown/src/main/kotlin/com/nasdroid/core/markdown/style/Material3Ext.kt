package com.nasdroid.core.markdown.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

/**
 * Constructs a [TextStyles] using recommended defaults from your Material 3 theme.
 */
@Composable
fun m3TextStyles(): TextStyles {
    return TextStyles(
        textStyle = MaterialTheme.typography.bodyLarge,
        headline1 = MaterialTheme.typography.displaySmall,
        headline2 = MaterialTheme.typography.headlineLarge,
        headline3 = MaterialTheme.typography.headlineMedium,
        headline4 = MaterialTheme.typography.headlineSmall,
        headline5 = MaterialTheme.typography.titleLarge,
        headline6 = MaterialTheme.typography.titleMedium,
    )
}

/**
 * Constructs a [TextStyleModifiers] using recommended defaults from your Material 3 theme.
 */
@Composable
fun m3TextStyleModifiers(): TextStyleModifiers {
    val primaryColor = MaterialTheme.colorScheme.primary
    return TextStyleModifiers(
        bold = { it.copy(fontWeight = FontWeight.Bold) },
        italics = { it.copy(fontStyle = FontStyle.Italic) },
        strikethrough = { it.copy(textDecoration = TextDecoration.LineThrough) },
        link = { it.copy(color = primaryColor, textDecoration = TextDecoration.Underline) },
        code = { it.copy(fontFamily = FontFamily.Monospace) }
    )
}
