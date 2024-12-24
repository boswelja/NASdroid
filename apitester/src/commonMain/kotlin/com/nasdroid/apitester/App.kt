package com.nasdroid.apitester

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import kotlinx.coroutines.launch

@Composable
fun App(
    client: DdpWebsocketClient
) {
    val coroutineScope = rememberCoroutineScope()
    MaterialTheme {
        when (client.state) {
            is DdpWebsocketClient.State.Connected -> {
                TODO("Tester")
            }
            is DdpWebsocketClient.State.Connecting -> {
                ConnectingScreen()
            }
            DdpWebsocketClient.State.Disconnected -> {
                ConnectScreen(
                    onConnect = { url, session ->
                        coroutineScope.launch {
                            client.connect(url, session)
                        }
                    }
                )
            }
        }
    }
}
