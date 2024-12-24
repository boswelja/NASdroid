package com.nasdroid.apitester

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient

fun main() = application {
    val state = rememberWindowState()
    val client = remember { DdpWebsocketClient() }
    Window(title = "TrueNAS API Tester", onCloseRequest = ::exitApplication, state = state) {
        App(client)
    }
}
