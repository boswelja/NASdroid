package com.nasdroid.apitester

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.nasdroid.api.websocket.jsonrpc.JsonRpcWebsocketClient
import com.nasdroid.apitester.connect.ConnectScreen
import com.nasdroid.apitester.connect.ConnectingScreen
import kotlinx.coroutines.launch

@Composable
fun App(
    client: JsonRpcWebsocketClient
) {
    val coroutineScope = rememberCoroutineScope()
    val state by client.state.collectAsState()
    MaterialTheme {
        when (state) {
            is JsonRpcWebsocketClient.State.Connected -> {
                TesterScreen(client)
            }
            JsonRpcWebsocketClient.State.Connecting -> {
                ConnectingScreen()
            }
            JsonRpcWebsocketClient.State.Disconnected -> {
                ConnectScreen(
                    onConnect = { url ->
                        coroutineScope.launch {
                            client.connect(url)
                        }
                    }
                )
            }
        }
    }
}
