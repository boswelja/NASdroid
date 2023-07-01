package com.boswelja.truemanager.dashboard.ui.overview.cards.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A linear progress indicator that is capable of showing many progress values at once. Colors will
 * rotate between values provided. Note that progress values adding to over 1f will still attempt to
 * display.
 */
@Composable
fun LinearMultiProgressIndicator(
    progresses: List<Float>,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    ),
) {
    val remainingProgress by remember {
        derivedStateOf {
            (1 - progresses.sum()).coerceIn(0f, 1f)
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(modifier)
    ) {
        progresses.forEachIndexed { index, progress ->
            val shape = if (index == progresses.lastIndex) {
                MaterialTheme.shapes.medium.copy(
                    topStart = ZeroCornerSize,
                    bottomStart = ZeroCornerSize,
                )
            } else {
                RectangleShape
            }
            val color = colors[index % colors.size]
            Box(
                modifier = Modifier
                    .weight(progress)
                    .fillMaxHeight()
                    .background(
                        color = color,
                        shape = shape
                    )
            )
        }
        if (remainingProgress > 0) Spacer(Modifier.weight(remainingProgress))
    }
}

@Preview
@Composable
fun LinearMultiProgressIndicatorPreview() {
    LinearMultiProgressIndicator(
        progresses = listOf(
            0.15f,
            0.55f,
            0.1f,
            0.05f
        )
    )
}
