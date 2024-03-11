package com.nasdroid.api.v2.apiKey

import com.nasdroid.api.ApiStateProvider
import com.nasdroid.api.Authorization
import com.nasdroid.api.InMemoryApiStateProvider
import com.nasdroid.api.getHttpClient
import com.nasdroid.api.readBinaryResource
import com.nasdroid.api.v2.apikey.AllowRule
import com.nasdroid.api.v2.apikey.ApiKey
import com.nasdroid.api.v2.apikey.ApiKeyV2ApiImpl
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiKeyV2ApiImplTest {

    private lateinit var apiStateProvider: ApiStateProvider

    @BeforeTest
    fun setUp() {
        apiStateProvider = InMemoryApiStateProvider().apply {
            serverAddress = "http://truenas.local/"
            authorization = Authorization.ApiKey("my.api.key")
        }
    }

    @Test
    fun `test getAll`() = runTest {
        val testResources = mapOf(
            readBinaryResource("/api/v2/apiKey/api_key_success.json") to listOf(
                ApiKey(
                    id = 17,
                    name = "TrueManager for TrueNAS",
                    createdAt = Instant.fromEpochMilliseconds(1693635311000),
                    allowList = listOf(
                        AllowRule("*", "*")
                    )
                )
            )
        )

        testResources.forEach {
            val engine = MockEngine {_ ->
                respond(
                    content = it.key,
                    status = HttpStatusCode.OK
                )
            }
            val subject = ApiKeyV2ApiImpl(
                getHttpClient(apiStateProvider = apiStateProvider, engine = engine)
            )
            assertEquals(
                it.value,
                subject.getAll(null, null, null)
            )
        }
    }
}
