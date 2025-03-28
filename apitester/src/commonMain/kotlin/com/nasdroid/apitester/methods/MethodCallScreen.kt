package com.nasdroid.apitester.methods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient

@Composable
fun MethodCallScreen(
    client: DdpWebsocketClient,
    modifier: Modifier = Modifier,
    viewModel: MethodCallViewModel = viewModel { MethodCallViewModel(client) },
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    val interactions by viewModel.interactions.collectAsState()
    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        Row(modifier) {
            MethodCallInputContent(
                onCallMethod = viewModel::callMethod,
                modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f)
            )
            VerticalDivider()
            MethodCallInteractionContent(
                interactions = interactions,
                modifier = Modifier.fillMaxHeight().fillMaxWidth()
            )
        }
    } else {
        Column(modifier) {
            MethodCallInputContent(
                onCallMethod = viewModel::callMethod,
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f)
            )
            HorizontalDivider()
            MethodCallInteractionContent(
                interactions = interactions,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )
        }
    }
}
