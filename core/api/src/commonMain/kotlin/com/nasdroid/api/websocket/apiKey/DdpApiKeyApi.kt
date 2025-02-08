package com.nasdroid.api.websocket.apiKey

import com.nasdroid.api.websocket.ddp.DdpWebsocketClient
import com.nasdroid.api.websocket.ddp.callMethod
import kotlinx.serialization.Serializable

/**
 * An implementation of [ApiKeyApi] that is backed by the TrueNAS DDP Websocket API.
 */
class DdpApiKeyApi(
    private val client: DdpWebsocketClient
) : ApiKeyApi {
    override suspend fun create(newKey: NewApiKey): ApiKey {
        return client.callMethod("api_key.create", listOf(newKey))
    }

    override suspend fun delete(id: Int): Boolean {
        return client.callMethod("api_key.delete", listOf(id))
    }

    override suspend fun getInstance(id: Int): ApiKey {
        return client.callMethod("api_key.get_instance", listOf(id))
    }

    override suspend fun update(
        id: Int,
        updatedKey: UpdateApiKey
    ): ApiKey {
        return client.callMethod(
            "api_key.update",
            listOf(UpdateApiKeyParams.Id(id), UpdateApiKeyParams.UpdateParams(updatedKey))
        )
    }
}

@Serializable
internal sealed interface UpdateApiKeyParams {

    @JvmInline
    @Serializable
    value class Id(val id: Int): UpdateApiKeyParams

    @Serializable
    @JvmInline
    value class UpdateParams(val params: UpdateApiKey): UpdateApiKeyParams
}