package com.nasdroid.api.websocket.auth

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed interface GenerateTokenParams

@Serializable
@JvmInline
value class TimeToLive(val seconds: Long) : GenerateTokenParams

@Serializable
@JvmInline
value class TokenAttributes(val attrs: Map<String, @Contextual Any>): GenerateTokenParams

@Serializable
@JvmInline
value class MatchOrigin(val matchOrigin: Boolean): GenerateTokenParams