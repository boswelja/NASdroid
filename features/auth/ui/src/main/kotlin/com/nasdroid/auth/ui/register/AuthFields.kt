package com.nasdroid.auth.ui.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

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
    error: Boolean = false,
    enabled: Boolean = true,
) {
    AnimatedContent(
        targetState = authData is AuthData.ApiKey,
        label = "Auth Mode",
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.large)) {
            if (it) {
                val apiKeyAuthData = authData as? AuthData.ApiKey ?: AuthData.ApiKey("")
                ApiKeyField(
                    apiKey = apiKeyAuthData.key,
                    onApiKeyChange ={ onAuthDataChange(apiKeyAuthData.copy(key = it)) },
                    onDone = onDone,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    error = error
                )
                SwitchToBasicAuth(
                    onClick = { onAuthDataChange(AuthData.Basic("", "")) },
                    enabled = enabled
                )
            } else {
                val basicAuthData = authData as? AuthData.Basic ?: AuthData.Basic("", "")
                BasicAuthFields(
                    username = basicAuthData.username,
                    onUsernameChange = { onAuthDataChange(basicAuthData.copy(username = it)) },
                    password = basicAuthData.password,
                    onPasswordChange = { onAuthDataChange(basicAuthData.copy(password = it)) },
                    onDone = onDone,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    error = error
                )
                SwitchToApiKey(
                    onClick = { onAuthDataChange(AuthData.ApiKey("")) },
                    enabled = enabled
                )
            }
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
     */
    data class Basic(val username: String, val password: String): AuthData
}

@Composable
internal fun SwitchToBasicAuth(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(modifier) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialThemeExt.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.no_like_api_key),
                color = MaterialThemeExt.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = onClick,
                enabled = enabled
            ) {
                Text(stringResource(R.string.switch_basic_auth))
            }
        }
    }
}

@Composable
internal fun SwitchToApiKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(modifier) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialThemeExt.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.basic_auth_warning),
                color = MaterialThemeExt.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = onClick,
                enabled = enabled
            ) {
                Text(stringResource(R.string.switch_api_key))
            }
        }
    }
}