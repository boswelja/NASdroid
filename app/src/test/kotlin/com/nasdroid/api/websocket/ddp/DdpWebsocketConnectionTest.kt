package com.nasdroid.api.websocket.ddp

import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DdpWebsocketConnectionTest {
    @Test
    fun connectWorks() = runTest {
        val connection = DdpWebsocketConnection(url = "ws://truenas.local/websocket")
        connection.connect()
        println(connection.state)
        connection.disconnect()
    }
}
