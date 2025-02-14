package com.nasdroid.auth.ui.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.nasdroid.auth.ui.R
import com.nasdroid.design.MaterialThemeExt

/**
 * A set of text fields that allows the user to input a username and password for authentication.
 */
@Composable
fun BasicAuthFields(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    createApiKey: Boolean,
    onCreateApiKeyChange: (Boolean) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialThemeExt.paddings.medium)
    ) {
        UsernameTextField(
            username = username,
            onUsernameChange = onUsernameChange,
            enabled = enabled,
            error = error,
            modifier = Modifier.fillMaxWidth()
        )
        PasswordTextField(
            password = password,
            onPasswordChange = onPasswordChange,
            onDone = onDone,
            enabled = enabled,
            error = error,
            modifier = Modifier.fillMaxWidth()
        )
        CreateApiKeyToggle(
            checked = createApiKey,
            onCheckedChange = onCreateApiKeyChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
internal fun UsernameTextField(
    username: String,
    onUsernameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
) {
    TextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text(stringResource(R.string.username_label)) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        leadingIcon = {
            Icon(Icons.Default.Person, null)
        },
        singleLine = true,
        enabled = enabled,
        isError = error,
        modifier = modifier
    )
}

@Composable
internal fun PasswordTextField(
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
        isError = error,
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
        supportingText = if (error) {{
            Text(stringResource(R.string.invalid_basic_auth))
        }} else { null },
        modifier = modifier
    )
}

@Composable
internal fun CreateApiKeyToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(stringResource(R.string.create_api_key_title)) },
        supportingContent = {
            if (checked) {
                Text(stringResource(R.string.create_api_key_yes_desc))
            } else {
                Text(stringResource(R.string.create_api_key_no_desc))
            }
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null
            )
        },
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .then(modifier)
    )
}
