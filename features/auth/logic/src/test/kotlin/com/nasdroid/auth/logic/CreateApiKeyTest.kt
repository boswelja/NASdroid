package com.nasdroid.auth.logic

import com.nasdroid.api.v2.apikey.ApiKeyV2Api
import com.nasdroid.api.v2.apikey.NewApiKey
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateApiKeyTest {
    private lateinit var apiState: com.nasdroid.api.v2.ApiStateProvider
    private lateinit var apiKeyApi: ApiKeyV2Api

    private lateinit var testSubject: CreateApiKey

    @BeforeTest
    fun setUp() {
        apiState = mockk(relaxed = true)
        apiKeyApi = mockk()

        testSubject = CreateApiKey(apiState, apiKeyApi)
    }

    @Test
    fun `when key is created successfully, key is returned`() = runTest {
        val testKey = NewApiKey(
            id = 1,
            name = "My new key",
            createdAt = Instant.DISTANT_FUTURE,
            allowList = emptyList(),
            key = "my-api-key"
        )
        coEvery { apiKeyApi.create(any(), any()) } returns testKey

        val result = testSubject(
            serverAddress = "http://server.address",
            username = "Username",
            password = "Password",
            keyName = "My new key"
        )
        assertTrue(result.isSuccess)
        assertEquals(testKey.key, result.getOrNull())
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        val testException = IOException("401 Unauthorized")
        coEvery { apiKeyApi.create(any(), any()) } throws testException

        val result = testSubject(
            serverAddress = "http://server.address",
            username = "Username",
            password = "Password",
            keyName = "My new key"
        )
        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }
}
