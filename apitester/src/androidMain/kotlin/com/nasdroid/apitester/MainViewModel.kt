package com.nasdroid.apitester

import androidx.lifecycle.ViewModel
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient

class MainViewModel : ViewModel() {
    val client = DdpWebsocketClient()
}
