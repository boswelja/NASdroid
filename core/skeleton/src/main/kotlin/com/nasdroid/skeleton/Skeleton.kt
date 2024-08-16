package com.nasdroid.skeleton

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

/**
 * An implementation of Material 3 [Skeleton Loaders](https://m3.material.io/styles/motion/transitions/transition-patterns#f7ff608a-087d-4a4e-9e83-f1af69184487).
 * This Composable allows you to place a skeleton shape in your Composition hierarchy.
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    baseColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    pulseColor: Color = baseColor.copy(alpha = 0.3f),
    pulseInMillis: Int = 500,
) {
    Box(modifier.skeleton(true, shape, baseColor, pulseColor, pulseInMillis))
}

@PreviewLightDark
@Composable
fun SkeletonPreview() {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold {
            Column(Modifier.padding(it).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Skeleton(
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp)
                )
                Skeleton(Modifier.fillMaxWidth().aspectRatio(16/9f))
            }
        }
    }
}
