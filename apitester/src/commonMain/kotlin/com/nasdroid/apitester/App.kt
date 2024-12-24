package com.nasdroid.apitester

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient

@Composable
fun App(
    client: DdpWebsocketClient
) {
    MaterialTheme {
        Scaffold {
            when (client.state) {
                is DdpWebsocketClient.State.Connected -> {
                    TODO("Tester")
                }
                is DdpWebsocketClient.State.Connecting -> {
                    TODO("Loading")
                }
                DdpWebsocketClient.State.Disconnected -> {
                    TODO("Login")
                }
            }
        }
    }
}
