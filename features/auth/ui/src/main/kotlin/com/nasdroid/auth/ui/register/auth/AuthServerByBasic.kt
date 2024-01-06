package com.nasdroid.auth.ui.register.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

/**
 * An opinionated set of Composables that asks the user for a username and password, and submits
 * them via [onLoginWithBasic] when they are done.
 */
@Composable
fun AuthServerByBasic(
    onLoginWithBasic: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val (username, onUsernameChange) = rememberSaveable {
        mutableStateOf("")
    }
    val (password, onPasswordChange) = rememberSaveable {
        mutableStateOf("")
    }
    val canLogIn by remember(username, password, enabled) {
        derivedStateOf { enabled && username.isNotBlank() && password.isNotBlank() }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        BasicAuthFields(
            username = username,
            onUsernameChange = onUsernameChange,
            password = password,
            onPasswordChange = onPasswordChange,
            onDone = {
                if (canLogIn) onLoginWithBasic(username, password)
            },
            enabled = enabled,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        LoginButton(
            onClick = { if (canLogIn) onLoginWithBasic(username, password) },
            enabled = canLogIn,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
internal fun BasicAuthFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
) {
    var isPasswordHidden by rememberSaveable {
        mutableStateOf(true)
    }
    Column(modifier) {
        AnimatedVisibility(
            visible = error,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialThemeExt.colorScheme.error) {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Error, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.invalid_basic_auth),
                        style = MaterialThemeExt.typography.labelLarge,
                        color = MaterialThemeExt.colorScheme.error
                    )
                }
            }
        }
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.username_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(Icons.Default.Person, null)
            },
            singleLine = true,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                defaultKeyboardAction(ImeAction.Done)
                onDone()
            },
            singleLine = true,
            enabled = enabled,
            visualTransformation = if (isPasswordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                IconButton(onClick = { isPasswordHidden = !isPasswordHidden }) {
                    if (isPasswordHidden) {
                        Icon(Icons.Default.Visibility, null)
                    } else {
                        Icon(Icons.Default.VisibilityOff, null)
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Default.Password, null)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
