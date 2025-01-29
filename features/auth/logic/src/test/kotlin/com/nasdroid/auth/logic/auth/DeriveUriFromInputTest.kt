package com.nasdroid.auth.logic.auth

import com.nasdroid.core.strongresult.StrongResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DeriveUriFromInputTest {

    private lateinit var deriveUri: DeriveUriFromInput

    @BeforeTest
    fun setUp() {
        deriveUri = DeriveUriFromInput()
    }

    @Test
    fun `when full formed input is passed, then uri is not transformed`() {
        val testCases = listOf(
            "ws://truenas.local/websocket",
            "wss://truenas.local/websocket",
            "ws://my.truenas.install/websocket",
            "ws://truenas.install/path/to/websocket/api",
            "wss://192.168.1.1/websocket",
        )

        testCases.forEach {
            assertEquals(
                StrongResult.success(it),
                deriveUri(it)
            )
        }
    }

    @Test
    fun `when part formed uri is passed, then uri is transformed`() {
        val testCases = mapOf(
            "truenas.local" to "ws://truenas.local/websocket",
            "truenas.install" to "ws://truenas.install/websocket",
            "wss://truenas.local" to "wss://truenas.local/websocket",
            "truenas.install/path/to/websocket/api" to "ws://truenas.install/path/to/websocket/api",
            "ws://192.168.1.1" to "ws://192.168.1.1/websocket",
            "192.168.1.1" to "ws://192.168.1.1/websocket",
        )

        testCases.forEach { (input, expected) ->
            assertEquals(
                StrongResult.success(expected),
                deriveUri(input)
            )
        }
    }

    @Test
    fun `when uri with http scheme is passed, then ws scheme is returned`() {
        val testCases = mapOf(
            "http://truenas.local/websocket" to "ws://truenas.local/websocket",
            "https://truenas.local/websocket" to "wss://truenas.local/websocket",
            "https://192.168.1.1/websocket" to "wss://192.168.1.1/websocket",
        )

        testCases.forEach { (input, expected) ->
            assertEquals(
                StrongResult.success(expected),
                deriveUri(input)
            )
        }
    }

    @Test
    fun `when unknown scheme passed, then error is returned`() {
        val testCases = listOf(
            "ftp://truenas.local",
            "ssh://truenas.local/websocket"
        )

        testCases.forEach {
            assertEquals(
                StrongResult.failure(DeriveUriError.InvalidScheme),
                deriveUri(it)
            )
        }
    }

    @Test
    fun `when invalid uri passed, then error is returned`() {
        val testCases = listOf(
            "invalid uri",
            "another invalid uri",
            "    "
        )

        testCases.forEach {
            assertEquals(
                StrongResult.failure(DeriveUriError.InvalidUri),
                deriveUri(it)
            )
        }
    }
}
