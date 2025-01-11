package com.nasdroid.apitester.methods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient

@Composable
fun MethodCallScreen(
    client: DdpWebsocketClient,
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        Row(modifier) {
            MethodCallInputContent(
                onCallMethod = {},
                modifier = Modifier.fillMaxHeight().weight(1f)
            )
            VerticalDivider()
            MethodCallInteractionContent(
                interactions = emptyList(),
                modifier = Modifier.fillMaxHeight().weight(1f)
            )
        }
    } else {
        Column(modifier) {
            MethodCallInputContent(
                onCallMethod = {},
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            VerticalDivider()
            MethodCallInteractionContent(
                interactions = emptyList(),
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }
}
