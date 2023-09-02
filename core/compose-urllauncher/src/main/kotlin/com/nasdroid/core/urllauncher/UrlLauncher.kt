package com.nasdroid.core.urllauncher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * A contract that allows launching a URL, ideally from a Compose side-effect. See [launchUrl] for
 * details.
 */
interface UrlLauncher {

    /**
     * "Launches" the given URL in that platform-preferred manner. For example, on Android a Custom
     * Tab might be launched.
     */
    fun launchUrl(url: String)
}

/**
 * Remembers the default URL launcher implementation.
 */
@Composable
fun rememberUrlLauncher(): UrlLauncher {
    val context = LocalContext.current
    return remember(context) {
        DefaultUrlLauncher(context)
    }
}
