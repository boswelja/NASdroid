package com.nasdroid.api.websocket.ddp.message

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * This file contains concrete definitions for
 * [DDP errors](https://github.com/meteor/meteor/blob/devel/packages/ddp/DDP.md#errors).
 */

/**
 * Received from the server indicating a contract issue. Some examples include:
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
internal data class ErrorMessage(
    @SerialName("reason")
    val reason: String,
    @SerialName("offendingMessage")
    val requestMessage: ClientMessage? = null
) : ServerMessage {
    @OptIn(ExperimentalSerializationApi::class)
    @SerialName("msg")
    @EncodeDefault
    val msg: String = "error"
}

@Serializable
internal data class Error(
    @SerialName("error")
    val error: Int,
    @SerialName("errname")
    val errorName: String,
    @SerialName("type")
    val errorType: String?,
    @SerialName("reason")
    val reason: String? = null,
    @SerialName("trace")
    val message: String? = null,
    @SerialName("extra")
    val extra: String? = null
)
