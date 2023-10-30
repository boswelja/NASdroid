package com.nasdroid.auth.ui.register.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

    AnimatedContent(targetState = authMode, label = "Auth Mode") {
        when (it) {
            AuthMode.ApiKey -> {
                AuthServerByKey(
                    onLoginWithKey = {
                        viewModel.logIn(it)
                    },
                    modifier = modifier.padding(contentPadding)
                )
            }
            AuthMode.Basic -> {
                AuthServerByBasic(
                    onLoginWithBasic = { username, password ->
                        viewModel.logIn(username, password)
                    },
                    modifier = modifier.padding(contentPadding)
                )
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

enum class AuthMode {
    ApiKey,
    Basic
}
