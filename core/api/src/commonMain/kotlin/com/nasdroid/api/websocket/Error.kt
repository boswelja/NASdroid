package com.nasdroid.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/*
 * This file contains concrete definitions for
 * [DDP errors](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#errors).
 */

/**
 * Received from the server indicating a contract issue, including but not limited to:
 *
 * - Sending messages which are not valid JSON objects
 * - Unknown msg type
 * - Other malformed client requests (not including required fields)
 * - Sending anything other than connect as the first message, or sending connect as a non-initial message
 *
 * @property reason A string describing the error.
 * @property requestMessage If the original message is parsed properly, it is represented here.
 */
@Serializable
data class ErrorMessage(
    @SerialName("reason")
    val reason: String,
    @SerialName("offendingMessage")
    val requestMessage: JsonElement? = null
) {
    @SerialName("msg")
    val msg: String = "error"
}

@Serializable
data class Error(
    @SerialName("error")
    val error: String,
    @SerialName("errorType")
    val errorType: String,
    @SerialName("reason")
    val reason: String? = null,
    @SerialName("message")
    val message: String? = null,
)
