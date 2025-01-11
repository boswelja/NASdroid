package com.nasdroid.apitester.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nasdroid.apitester.InteractionListItem

@Composable
fun MethodCallInteractionContent(
    interactions: List<MethodInteraction>,
    modifier: Modifier = Modifier.Companion,
    contentPadding: PaddingValues = PaddingValues()
) {
    SelectionContainer {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Bottom
        ) {
            items(interactions) {
                when (it) {
                    is MethodInteraction.CallMethod -> {
                        InteractionListItem(
                            leadingContent = {
                                Icon(Icons.AutoMirrored.Default.ArrowForward, null)
                            }
                        ) {
                            Text("${it.method}: ${it.params}")
                        }
                    }

                    is MethodInteraction.MethodCallResult -> {
                        InteractionListItem(
                            leadingContent = {
                                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                            }
                        ) {
                            Text(it.result.toString())
                        }
                    }
                }
            }
        }
    }
}
