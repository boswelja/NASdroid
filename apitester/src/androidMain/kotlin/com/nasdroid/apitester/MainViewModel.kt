package com.nasdroid.apitester

import androidx.lifecycle.ViewModel
import com.nasdroid.api.websocket.jsonrpc.JsonRpcWebsocketClient
import kotlinx.coroutines.runBlocking

class MainViewModel : ViewModel() {
    val client = JsonRpcWebsocketClient()

    override fun onCleared() {
        super.onCleared()
        // Must disconnect
        runBlocking {
            client.disconnect()
        }
    }
}
