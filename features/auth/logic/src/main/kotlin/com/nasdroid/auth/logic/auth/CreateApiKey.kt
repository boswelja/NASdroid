package com.nasdroid.auth.logic.auth

import com.nasdroid.api.websocket.apiKey.AllowedMethod
import com.nasdroid.api.websocket.apiKey.ApiKeyApi
import com.nasdroid.api.websocket.apiKey.NewApiKey
import com.nasdroid.api.websocket.ddp.MethodCallError
import com.nasdroid.core.strongresult.StrongResult

/**
 * Creates an API key. Intended for use before users have logged in. See [invoke] for details.
 */
class CreateApiKey(
    private val apiKeyApi: ApiKeyApi
) {

    /**
     * Creates an API key using the provided information, or a [CreateApiKeyError] if something goes
     * wrong.
     */
    suspend operator fun invoke(
        name: String,
    ): StrongResult<String, CreateApiKeyError> {
        return try {
            val key = apiKeyApi.create(NewApiKey(name, listOf(AllowedMethod(AllowedMethod.Method.All, "*"))))
            StrongResult.success(key.key)
        } catch (e: MethodCallError) {
            // Not really sure what all possible causes of this are, but I do know it'll happen with
            // duplicate names.
            when (e.error) {
                "207" -> StrongResult.failure(CreateApiKeyError.InvalidCredentials)
                else -> StrongResult.failure(CreateApiKeyError.KeyAlreadyExists)
            }
        }
    }
}

/**
 * Encapsulates all possible failure modes for [CreateApiKey].
 */
sealed interface CreateApiKeyError {
    /**
     * The credentials that were provided to create the key with were not valid.
     */
    data object InvalidCredentials : CreateApiKeyError

    /**
     * An API key with the same name already exists.
     */
    data object KeyAlreadyExists : CreateApiKeyError
}
