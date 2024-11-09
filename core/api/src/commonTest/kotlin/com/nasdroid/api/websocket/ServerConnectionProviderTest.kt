package com.nasdroid.api.websocket

import com.nasdroid.api.websocket.message.ConnectMessage
import com.nasdroid.api.websocket.message.MethodMessage
import com.nasdroid.api.websocket.message.ResultMessage
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ServerConnectionProviderTest {

    @BeforeTest
    fun setUp() {

    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun works() = runTest {
        val result = ServerConnectionProvider(this.backgroundScope, "admin", "ZgA3NkLYye", "192.168.8.200", null)
            .connection
            .map {
                val id = Uuid.random().toString()
                it.sendSerialized(
                    MethodMessage(
                        id = id,
                        method = "system.boot_id",
                        params = null
                    )
                )
                it.receiveDeserialized<ResultMessage<String>>()
            }
            .first()
        println(result)
    }
}
