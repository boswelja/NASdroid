package com.nasdroid.apitester

import androidx.lifecycle.ViewModel
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    val client = DdpWebsocketClient()

    override fun onCleared() {
        super.onCleared()
        // Must disconnect
        runBlocking {
            client.disconnect()
        }
    }
}
