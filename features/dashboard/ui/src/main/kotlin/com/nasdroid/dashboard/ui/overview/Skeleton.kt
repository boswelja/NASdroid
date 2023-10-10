package com.nasdroid.dashboard.ui.overview

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * A simple implementation of [Material3 skeleton loaders](https://m3.material.io/styles/motion/transitions/transition-patterns#f7ff608a-087d-4a4e-9e83-f1af69184487).
 * Note this implementation does not currently support in-progress animations. Animating from
 * loading to done will be faded though.
 */
@Composable
fun Modifier.skeleton(
    loading: Boolean,
    color: Color = MaterialTheme.colorScheme.surface,
    cornerRadius: CornerSize = CornerSize(8.dp)
): Modifier {
    val drawnColor by animateColorAsState(if (loading) color else color.copy(alpha = 0f), label = "skeleton")
    val density = LocalDensity.current
    return drawWithContent {
        drawContent()
        val cornerSize = cornerRadius.toPx(shapeSize = this.size, density = density)
        drawRoundRect(color = drawnColor, cornerRadius = CornerRadius(x = cornerSize, y = cornerSize))
    }
}
