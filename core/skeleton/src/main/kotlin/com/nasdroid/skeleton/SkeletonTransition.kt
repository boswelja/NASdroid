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
import androidx.compose.ui.graphics.Color

@Composable
fun rememberSkeletonAnimation(
    baseColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    pulseColor: Color = baseColor.copy(alpha = 0.3f), // TODO Don't use alpha changes
    pulseInMillis: Int = 500
): Color {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = baseColor,
        targetValue = pulseColor,
        animationSpec = infiniteRepeatable(
            animation = tween(pulseInMillis, easing = LinearEasing, delayMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    return color
}
