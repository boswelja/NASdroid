package com.nasdroid.auth.ui.register.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthServerScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AuthServerViewModel = koinViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    LaunchedEffect(loginState) {
        if (loginState == LoginState.Success) {
            onLoginSuccess()
        }
    }

    var authMode by rememberSaveable { mutableStateOf(AuthMode.ApiKey) }

    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        // TODO This is just a temporary title. It should be a proper TopAppBar with a back button.
        Text(
            text = "Log In",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(Modifier.weight(1f))
        AnimatedContent(
            targetState = authMode,
            label = "Auth Mode",
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            when (it) {
                AuthMode.ApiKey -> {
                    AuthServerByKey(
                        onLoginWithKey = { apiKey ->
                            viewModel.logIn(apiKey)
                        }
                    )
                }
                AuthMode.Basic -> {
                    AuthServerByBasic(
                        onLoginWithBasic = { username, password ->
                            viewModel.logIn(username, password)
                        }
                    )
                }
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSecondaryContainer
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.no_like_api_key))
            }
        }

        TextButton(
            onClick = { authMode = AuthMode.Basic }
        ) {
            Text(stringResource(R.string.switch_basic_auth))
        }
    }
}

@Composable
internal fun LoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    ) {
        if (loading) {
            CircularProgressIndicator(Modifier.size(26.dp))
        } else {
            Text(stringResource(R.string.log_in))
        }
    }
}

enum class AuthMode {
    ApiKey,
    Basic
}
