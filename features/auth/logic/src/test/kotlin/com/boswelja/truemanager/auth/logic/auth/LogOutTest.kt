package com.boswelja.truemanager.auth.logic.auth

import com.boswelja.truemanager.core.api.v2.ApiStateProvider
import com.boswelja.truemanager.core.api.v2.Authorization
import io.mockk.every
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNull

class LogOutTest {
    private lateinit var stateProvider: ApiStateProvider
    private var authorization: Authorization? = null
    private var serverAddress: String? = null

    private lateinit var testSubject: LogOut

    @BeforeTest
    fun setUp() {
        stateProvider = mockk()
        every { stateProvider::authorization.set(any()) } answers {
            authorization = firstArg()
        }
        every { stateProvider::serverAddress.set(any()) } answers {
            serverAddress = firstArg()
        }

        testSubject = LogOut(stateProvider)
    }

    @Test
    fun `when invoked, authorization state is reset`() {
        authorization = Authorization.ApiKey("api-key")
        serverAddress = "https://server.address"

        testSubject()

        assertNull(authorization)
        assertNull(serverAddress)
    }
}