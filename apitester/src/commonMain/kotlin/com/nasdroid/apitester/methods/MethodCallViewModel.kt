package com.nasdroid.apitester.methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.MethodCallError
import com.nasdroid.api.websocket.ddp.callMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

class MethodCallViewModel(private val client: DdpWebsocketClient) : ViewModel() {
    private val _interactions = MutableStateFlow(emptyList<MethodInteraction>())
    val interactions: StateFlow<List<MethodInteraction>> = _interactions

    fun callMethod(call: MethodInteraction.CallMethod) {
        viewModelScope.launch {
            _interactions.update {
                it + call
            }
            val result = try {
                if (call.params.isEmpty()) {
                    client.callMethod<JsonElement>(call.method).toString()
                } else {
                    client.callMethod<JsonElement, JsonElement>(call.method, call.params).toString()
                }
            } catch (e: MethodCallError) {
                e.toString()
            }
            _interactions.update {
                it + MethodInteraction.MethodCallResult(result)
            }
        }
    }
}

sealed interface MethodInteraction {
    data class CallMethod(val method: String, val params: List<JsonElement>): MethodInteraction
    data class MethodCallResult(val result: String): MethodInteraction
}
