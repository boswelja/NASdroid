package com.boswelja.truemanager.core.logviewer

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.boswelja.truemanager.core.logviewer.color.blend.Blend

/**
 * Colors for various log components.
 *
 * @property debug The color of debug-level log lines.
 * @property info The color of info-level log lines.
 * @property warn The color of warning-level log lines.
 * @property error The color of error-level log lines.
 * @property timestamp The color of log line timestamps.
 * @property levelIndicator The color of the log level indicator text.
 */
data class LogColors(
    val debug: Color,
    val info: Color,
    val warn: Color,
    val error: Color,
    val timestamp: Color,
    val levelIndicator: Color,
)

@Suppress("MagicNumber")
internal object LogColorDefaults {
    val DebugColorLight = Color(0xff006782)
    val DebugColorDark = Color(0xff5ed4ff)
    val InfoColorLight = Color(0xff026e00)
    val InfoColorDark = Color(0xff02e600)
    val WarningColorLight = Color(0xff626200)
    val WarningColorDark = Color(0xffcdcd00)
}

/**
 * Retrieves a [LogColors] for Material3 themes. Colors support dark mode, and are harmonized
 * towards [themePrimaryColor].
 */
@Composable
fun rememberMaterial3LogColors(
    isSystemInDarkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    themePrimaryColor: Color = MaterialTheme.colorScheme.primary
): LogColors {
    val errorColor = MaterialTheme.colorScheme.error
    val timestampColor = LocalContentColor.current
    val levelIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.65f)
    return remember(isSystemInDarkTheme, themePrimaryColor) {
        val themeColorArgb = themePrimaryColor.toArgb()
        if (isSystemInDarkTheme) {
            LogColors(
                debug = Color(Blend.harmonize(LogColorDefaults.DebugColorDark.toArgb(), themeColorArgb)),
                info = Color(Blend.harmonize(LogColorDefaults.InfoColorDark.toArgb(), themeColorArgb)),
                warn = Color(Blend.harmonize(LogColorDefaults.WarningColorDark.toArgb(), themeColorArgb)),
                error = errorColor,
                timestamp = timestampColor,
                levelIndicator = levelIndicatorColor,
            )
        } else {
            LogColors(
                debug = Color(Blend.harmonize(LogColorDefaults.DebugColorLight.toArgb(), themeColorArgb)),
                info = Color(Blend.harmonize(LogColorDefaults.InfoColorLight.toArgb(), themeColorArgb)),
                warn = Color(Blend.harmonize(LogColorDefaults.WarningColorLight.toArgb(), themeColorArgb)),
                error = errorColor,
                timestamp = timestampColor,
                levelIndicator = levelIndicatorColor
            )
        }
    }
}
