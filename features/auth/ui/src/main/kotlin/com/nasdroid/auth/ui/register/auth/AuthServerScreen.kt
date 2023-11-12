package com.nasdroid.auth.ui.register.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

/**
 * A screen that allows the user to authenticate with a server they have already selected.
 */
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
        AnimatedContent(
            targetState = authMode,
            label = "Auth Mode",
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Bottom),
                modifier = Modifier.fillMaxSize()
            ) {
                when (it) {
                    AuthMode.ApiKey -> {
                        AuthServerByKey(
                            onLoginWithKey = viewModel::logIn
                        )
                        SwitchToBasicAuth(onClick = { authMode = AuthMode.Basic })
                    }
                    AuthMode.Basic -> {
                        AuthServerByBasic(
                            onLoginWithBasic = viewModel::logIn
                        )
                        SwitchToApiKey(onClick = { authMode = AuthMode.ApiKey })
                    }
                }
            }
        }
    }
}

@Composable
internal fun SwitchToBasicAuth(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.no_like_api_key),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = onClick
            ) {
                Text(stringResource(R.string.switch_basic_auth))
            }
        }
    }
}

@Composable
internal fun SwitchToApiKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
        Column {
            Text(
                text = stringResource(R.string.basic_auth_warning),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = onClick
            ) {
                Text(stringResource(R.string.switch_api_key))
            }
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

/**
 * Describes all available modes of authentication.
 */
enum class AuthMode {
    /**
     * Authentication via an API key.
     */
    ApiKey,

    /**
     * Authentication via a username and password.
     */
    Basic
}
