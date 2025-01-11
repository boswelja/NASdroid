package com.nasdroid.apitester

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Commit
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.apitester.methods.MethodCallScreen
import kotlinx.serialization.json.JsonElement

@Composable
fun TesterScreen(
    client: DdpWebsocketClient,
    modifier: Modifier = Modifier
) {
    var destination by rememberSaveable { mutableStateOf(TesterDestination.MethodCall) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = destination == TesterDestination.MethodCall,
                onClick = { destination = TesterDestination.MethodCall },
                icon = {
                    Icon(Icons.Default.Commit, null)
                },
                label = {
                    Text("Method Call")
                }
            )
            item(
                selected = destination == TesterDestination.Subscription,
                onClick = { destination = TesterDestination.Subscription },
                icon = {
                    Icon(Icons.Default.Commit, null)
                },
                label = {
                    Text("Subscription")
                }
            )
        },
        modifier = modifier
    ) {
        AnimatedContent(destination) {
            when (it) {
                TesterDestination.MethodCall -> {
                    MethodCallScreen(
                        client = client,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                TesterDestination.Subscription -> {

                }
            }
        }
    }
}

enum class TesterDestination {
    MethodCall,
    Subscription
}

@Composable
fun InteractionListItem(
    leadingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(Modifier.size(24.dp)) {
            leadingContent()
        }
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace
            ),
            content = content
        )
    }
}
