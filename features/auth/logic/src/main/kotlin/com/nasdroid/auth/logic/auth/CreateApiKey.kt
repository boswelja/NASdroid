package com.nasdroid.auth.logic.auth

import com.nasdroid.api.websocket.apiKey.AllowedMethod
import com.nasdroid.api.websocket.apiKey.ApiKeyApi
import com.nasdroid.api.websocket.apiKey.NewApiKey
import com.nasdroid.api.websocket.auth.AuthApi
import com.nasdroid.api.websocket.ddp.MethodCallError
import com.nasdroid.core.strongresult.StrongResult

/**
 * Creates an API key. Intended for use before users have logged in. See [invoke] for details.
 */
class CreateApiKey(
    private val authApi: AuthApi,
    private val apiKeyApi: ApiKeyApi
) {

    /**
     * Creates an API key using the provided information, or a [CreateApiKeyError] if something goes
     * wrong.
     */
    suspend operator fun invoke(
        name: String,
        username: String,
        password: String,
        otpToken: String? = null
    ): StrongResult<String, CreateApiKeyError> {
        return try {
            if (!authApi.logIn(username, password, otpToken)) {
                return StrongResult.failure(CreateApiKeyError.InvalidCredentials)
            }
            val key = apiKeyApi.create(NewApiKey(name, listOf(AllowedMethod(AllowedMethod.Method.All, "*"))))
            StrongResult.success(key.key)
        } catch (_: MethodCallError) {
            // Not really sure what all possible causes of this are, but I do know it'll happen with
            // duplicate names.
            StrongResult.failure(CreateApiKeyError.KeyAlreadyExists)
        } finally {
            authApi.logOut()
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
