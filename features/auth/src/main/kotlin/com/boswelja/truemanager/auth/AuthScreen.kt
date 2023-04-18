package com.boswelja.truemanager.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
) {
    // TODO Different layouts based on device type
    AuthPhoneContent(modifier = modifier)
}

@Composable
fun AuthPhoneContent(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        AuthFields(
            onLogIn = { _, _, _ -> },
            modifier = Modifier.fillMaxWidth().widthIn(max = 560.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthFields(
    onLogIn: (serverAddress: String, username: String, password: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var serverAddress by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val inputsValid by remember {
        derivedStateOf {
            serverAddress.isNotBlank() && username.isNotBlank() && password.isNotBlank()
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = serverAddress,
            onValueChange = { serverAddress = it },
            label = { Text("Server Address") },
            placeholder = { Text("https://truenas.local") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = remember { PasswordVisualTransformation() },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onLogIn(serverAddress, username, password) },
            enabled = inputsValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Log In")
        }
    }
}

@Preview
@Composable
fun AuthFieldsPreview() {
    AuthFields(
        onLogIn = { _, _, _ ->  }
    )
}
