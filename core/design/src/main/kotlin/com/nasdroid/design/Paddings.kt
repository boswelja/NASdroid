package com.nasdroid.design

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A paddings holds all the padding sizes used for [MaterialThemeExt].
 *
 * @property tiny The smallest padding size.
 * @property small A padding size that is larger than [tiny], but smaller than [medium].
 * @property medium A padding size that is larger than [small], but smaller than [large].
 * @property large A padding size that is larger than [medium], but smaller than [xLarge].
 * @property xLarge The largest padding size.
 */
data class Paddings(
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val xLarge: Dp
)

/**
 * Create a default set of padding values to be used throughout the app.
 */
fun defaultPaddings(): Paddings = Paddings(
    tiny = 4.dp,
    small = 8.dp,
    medium = 12.dp,
    large = 16.dp,
    xLarge = 24.dp
)

/**
 * CompositionLocal used to pass Paddings down the tree.
 * Setting the value here is typically done as part of [MaterialThemeExt]. To retrieve the current
 * value of this CompositionLocal, use [MaterialThemeExt.paddings].
 */
val LocalPaddings = staticCompositionLocalOf { defaultPaddings() }

/**
 * Adds [this] PaddingValues to [other] PaddingValues.
 */
@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return remember(this, other) {
        PaddingValues(
            start = this.calculateStartPadding(layoutDirection) + other.calculateStartPadding(layoutDirection),
            top = this.calculateTopPadding() + other.calculateTopPadding(),
            end = this.calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
            bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
        )
    }
}
