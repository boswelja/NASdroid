package com.boswelja.truemanager.auth.logic

import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.system.SystemV2Api
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TestServerTokenTest {

    private lateinit var systemV2Api: SystemV2Api
    private lateinit var apiState: ApiStateProvider

    private lateinit var testSubject: TestServerToken

    @BeforeTest
    fun setUp() {
        systemV2Api = mockk()
        apiState = mockk(relaxed = true)

        testSubject = TestServerToken(apiState, systemV2Api)
    }

    @Test
    fun `when api throws, error is returned`() = runTest {
        val testException = IOException("401 Unauthorized")
        coEvery { systemV2Api.getHostId() } throws testException
        val result = testSubject("http://test.server", "test-api-key")
        assertEquals(true, result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }

    @Test
    fun `when api returns nothing, error is returned`() = runTest {
        val nothingCases = listOf(
            "",
            " ",
        )
        nothingCases.forEach { nothing ->
            coEvery { systemV2Api.getHostId() } returns nothing
            val result = testSubject("http://test.server", "test-api-key")
            assertEquals(
                expected = true,
                actual = result.isFailure,
                message = "Token test did not fail when the server returned \"$nothing\""
            )
        }
    }

    @Test
    fun `when api returns id, success is returned`() = runTest {
        coEvery { systemV2Api.getHostId() } returns "some-server-id"
        val result = testSubject("http://test.server", "test-api-key")
        assertEquals(true, result.isSuccess)
    }
}
