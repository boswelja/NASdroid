package com.nasdroid.api.websocket.alert

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.callMethod

class DdpAlertApi(
    private val client: DdpWebsocketClient
) : AlertApi {
    override suspend fun dismiss(uuid: String) {
        return client.callMethod("alert.dismiss", listOf(uuid))
    }

    override suspend fun list(): List<Alert> {
        return client.callMethod("alert.list")
    }

    override suspend fun listCategories(): List<AlertCategory> {
        return client.callMethod("alert.list_categories")
    }

    override suspend fun listPolicies(): List<AlertPolicy> {
        return client.callMethod("alert.list_policies")
    }

    override suspend fun restore(uuid: String) {
        return client.callMethod("alert.restore", listOf(uuid))
    }
}
