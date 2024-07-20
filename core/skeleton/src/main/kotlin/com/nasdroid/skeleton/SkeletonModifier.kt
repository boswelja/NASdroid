package com.nasdroid.skeleton

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

/**
 * An implementation of Material 3 [Skeleton Loaders](https://m3.material.io/styles/motion/transitions/transition-patterns#f7ff608a-087d-4a4e-9e83-f1af69184487).
 * This Modifier allows you to effectively replace the content of a Composable with a skeleton.
 */
@Composable
fun Modifier.skeleton(
    loading: Boolean,
    shape: Shape = MaterialTheme.shapes.medium,
    baseColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    pulseColor: Color = baseColor.copy(alpha = 0.3f),
    pulseInMillis: Int = 500,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = baseColor,
        targetValue = pulseColor,
        animationSpec = infiniteRepeatable(
            animation = tween(pulseInMillis, easing = LinearEasing, delayMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    return clip(shape).drawWithContent {
        drawContent()
        drawRoundRect(color = color)
    }
}
