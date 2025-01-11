package com.nasdroid.apitester.methods

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.serialization.json.Json

@Composable
fun MethodCallInputContent(
    onCallMethod: (MethodInteraction.CallMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val (method, setMethod) = remember { mutableStateOf("") }
    var params = remember { mutableStateListOf<String>() }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onCallMethod(MethodInteraction.CallMethod(method, params.map { Json.decodeFromString(it) }))
                }
            ) {
                Text("Call method")
            }
        }
    ) {
        LazyColumn(modifier, contentPadding = it) {
            item {
                OutlinedTextField(
                    value = method,
                    onValueChange = setMethod,
                    label = {
                        Text("Method")
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Uri,
                        imeAction = if (params.isEmpty()) ImeAction.Done else ImeAction.Next
                    )
                )
            }
            itemsIndexed(params) { index, param ->
                Row {
                    OutlinedTextField(
                        value = param,
                        onValueChange = {
                            params[index] = it
                        },
                        label = {
                            Text("Parameter ${index + 1}")
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Uri,
                            imeAction = if (params.size - 1 > index) ImeAction.Done else ImeAction.Next
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { params.removeAt(index) }) { Icon(Icons.Default.Delete, null) }
                }
            }
            item {
                ListItem(
                    headlineContent = { Text("Add parameter") },
                    leadingContent = { Icon(Icons.Default.Add, null) },
                    modifier = Modifier.clickable { params.add("") }
                )
            }
        }
    }

}
