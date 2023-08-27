package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.auth.logic.TestServerToken
import com.boswelja.truemanager.auth.logic.manageservers.GetServerToken
import com.boswelja.truemanager.auth.logic.manageservers.Server
import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LogInTest {

    private lateinit var stateProvider: ApiStateProvider
    private lateinit var testServerToken: TestServerToken
    private lateinit var getServerToken: GetServerToken

    private lateinit var testSubject: LogIn

    @BeforeTest
    fun setUp() {
        stateProvider = mockk(relaxed = true)
        testServerToken = mockk()
        getServerToken = mockk()

        testSubject = LogIn(stateProvider, testServerToken, getServerToken)
    }

    @Test
    fun `when no token is stored, error is returned`() = runTest {
        val testException = IllegalStateException("A token for the server does not exist!")
        coEvery { getServerToken.invoke(any()) } returns Result.failure(testException)

        val result = testSubject(Server("server name", "https://server.url", "server id"))
        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }

    @Test
    fun `when a token is stored but is not valid, error is returned`() = runTest {
        val testException = IllegalStateException("The server token is not valid!")
        coEvery { getServerToken.invoke(any()) } returns Result.success("my-server-token")
        coEvery { testServerToken.invoke(any(), any()) } returns Result.failure(testException)

        val result = testSubject(Server("server name", "https://server.url", "server id"))
        assertTrue(result.isFailure)
        assertEquals(testException, result.exceptionOrNull())
    }

    @Test
    fun `when a token is stored and valid, success is returned`() = runTest {
        coEvery { getServerToken.invoke(any()) } returns Result.success("my-server-token")
        coEvery { testServerToken.invoke(any(), any()) } returns Result.success(Unit)

        val result = testSubject(Server("server name", "https://server.url", "server id"))
        assertTrue(result.isSuccess)
    }

    @Test
    fun `when success is returned, api state is authenticated`() = runTest {
        var authorization: Authorization? = null
        every { stateProvider::authorization.set(any()) } answers {
            authorization = firstArg()
        }
        coEvery { getServerToken.invoke(any()) } returns Result.success("my-server-token")
        coEvery { testServerToken.invoke(any(), any()) } returns Result.success(Unit)

        testSubject(Server("server name", "https://server.url", "server id"))
        assertNotNull(authorization)
    }

    @Test
    fun `when failed is returned, api state is not authenticated`() = runTest {
        val testException = IllegalStateException("The server token is not valid!")
        var authorization: Authorization? = null
        every { stateProvider::authorization.set(any()) } answers {
            authorization = firstArg()
        }
        coEvery { getServerToken.invoke(any()) } returns Result.success("my-server-token")
        coEvery { testServerToken.invoke(any(), any()) } returns Result.failure(testException)

        testSubject(Server("server name", "https://server.url", "server id"))
        assertNull(authorization)
    }
}
