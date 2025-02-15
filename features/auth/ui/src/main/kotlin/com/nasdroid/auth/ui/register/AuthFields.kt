package com.nasdroid.auth.ui.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt
import com.nasdroid.design.NasDroidTheme

/**
 * Displays a column of fields that allow users to enter authentication data. See [AuthData] for
 * possible input modes.
 */
@Composable
fun AuthFields(
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    error: AuthError? = null,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthTypeSelector(
            authData = authData,
            onAuthDataChange = onAuthDataChange
        )
        AnimatedContent(
            targetState = authData is AuthData.ApiKey,
            label = "Auth Mode",
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = modifier
        ) { isApiKeyInput ->
            if (isApiKeyInput) {
                val apiKeyAuthData = authData as? AuthData.ApiKey ?: AuthData.ApiKey("")
                ApiKeyField(
                    apiKey = apiKeyAuthData.key,
                    onApiKeyChange = { onAuthDataChange(apiKeyAuthData.copy(key = it)) },
                    onDone = onDone,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    error = when (error) {
                        AuthError.FailedToCreateApiKey -> stringResource(R.string.error_failed_create_key)
                        AuthError.InvalidCredentials -> stringResource(R.string.error_invalid_key_auth)
                        null -> null
                    }
                )
            } else {
                val basicAuthData = authData as? AuthData.Basic ?: AuthData.Basic("", "", true)
                BasicAuthFields(
                    username = basicAuthData.username,
                    onUsernameChange = { onAuthDataChange(basicAuthData.copy(username = it)) },
                    password = basicAuthData.password,
                    onPasswordChange = { onAuthDataChange(basicAuthData.copy(password = it)) },
                    createApiKey = basicAuthData.isCreateApiKey,
                    onCreateApiKeyChange = { onAuthDataChange(basicAuthData.copy(isCreateApiKey = it)) },
                    onDone = onDone,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    error = when (error) {
                        AuthError.FailedToCreateApiKey -> stringResource(R.string.error_failed_create_key)
                        AuthError.InvalidCredentials -> stringResource(R.string.error_invalid_basic_auth)
                        null -> null
                    }
                )
            }
        }
    }
}

@Composable
internal fun AuthTypeSelector(
    authData: AuthData,
    onAuthDataChange: (AuthData) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier) {
        SegmentedButton(
            selected = authData is AuthData.ApiKey,
            onClick = { onAuthDataChange(AuthData.ApiKey("")) },
            shape = SegmentedButtonDefaults.itemShape(0, 2)
        ) {
            Text(stringResource(R.string.api_key_toggle), maxLines = 1)
        }
        SegmentedButton(
            selected = authData is AuthData.Basic,
            onClick = { onAuthDataChange(AuthData.Basic("", "", true)) },
            shape = SegmentedButtonDefaults.itemShape(1, 2)
        ) {
            Text(stringResource(R.string.password_toggle), maxLines = 1)
        }
    }
}

/**
 * Holds the current authentication data entered by the user. This can be either [ApiKey] or
 * [Basic].
 */
sealed interface AuthData {

    /**
     * Holds an API key for authentication.
     *
     * @property key The API key that the user has entered.
     */
    data class ApiKey(val key: String): AuthData

    /**
     * Holds a username and password for authentication.
     *
     * @property username The username that the user has entered.
     * @property password The password that the user has entered.
     * @property isCreateApiKey Whether an API key should be created using these credentials.
     */
    data class Basic(
        val username: String,
        val password: String,
        val isCreateApiKey: Boolean
    ): AuthData

    companion object {
        /**
         * A simple state saver for [AuthData]. This should be used in tandem with
         * [rememberSaveable] to avoid state loss on instance restore.
         */
        val Saver = run {
            val keyKey = "Key"
            val usernameKey = "Username"
            val passwordKey = "Password"
            val createKeyKey = "CreateApiKey"
            mapSaver<AuthData>(
                save = { authData ->
                    when (authData) {
                        is ApiKey -> mapOf(keyKey to authData.key)
                        is Basic -> mapOf(
                            usernameKey to authData.username,
                            passwordKey to authData.password,
                            createKeyKey to authData.isCreateApiKey
                        )
                    }
                },
                restore = { map ->
                    when {
                        map.contains(keyKey) -> ApiKey(map.getValue(keyKey).toString())
                        map.contains(usernameKey) -> Basic(
                            username = map.getValue(usernameKey).toString(),
                            password = map.getValue(passwordKey).toString(),
                            isCreateApiKey = map.getValue(createKeyKey) == true
                        )
                        else -> null
                    }
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthFieldsApiKeyPreview() {
    var authData by remember { mutableStateOf<AuthData>(AuthData.ApiKey("1234567890")) }
    NasDroidTheme {
        Surface {
            AuthFields(
                authData = authData,
                onAuthDataChange = { authData = it },
                onDone = {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AuthFieldsBasicPreview() {
    var authData by remember { mutableStateOf<AuthData>(AuthData.Basic("john.doe", "password", true)) }
    NasDroidTheme {
        Surface {
            AuthFields(
                authData = authData,
                onAuthDataChange = { authData = it },
                onDone = {},
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
