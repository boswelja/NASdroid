@file:Suppress("ForbiddenImport")
package com.nasdroid.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * Contains functions to access the current theme values provided at the call site's position in
 * the hierarchy. This extends [MaterialTheme] to add [paddings].
 */
object MaterialThemeExt {
    /**
     * Retrieves the current [ColorScheme] at the call site's position in the hierarchy.
     */
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    /**
     * Retrieves the current [Typography] at the call site's position in the hierarchy.
     */
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    /**
     * Retrieves the current [Shapes] at the call site's position in the hierarchy.
     */
    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    /**
     * Retrieves the current [Paddings] at the call site's position in the hierarchy.
     */
    val paddings: Paddings
        @Composable
        @ReadOnlyComposable
        get() = LocalPaddings.current
}

/**
 * An extension of [MaterialTheme] that also provides items from [MaterialThemeExt].
 */
@Composable
fun MaterialThemeExt(
    colorScheme: ColorScheme = MaterialThemeExt.colorScheme,
    shapes: Shapes = MaterialThemeExt.shapes,
    typography: Typography = MaterialThemeExt.typography,
    paddings: Paddings = MaterialThemeExt.paddings,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalPaddings provides paddings
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = typography,
            content = content
        )
    }
}
