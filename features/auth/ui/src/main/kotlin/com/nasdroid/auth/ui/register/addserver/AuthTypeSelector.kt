package com.nasdroid.auth.ui.register.addserver

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * A selector for different authentication types available to the user.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTypeSelector(
    currentType: AuthType,
    onAuthTypeChange: (AuthType) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        AuthTypes.forEachIndexed { index, authType ->
            val isSelected by remember(currentType) {
                derivedStateOf {
                    currentType == authType
                }
            }
            SegmentedButton(
                selected = isSelected,
                onClick = { onAuthTypeChange(authType) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = AuthTypes.size),
                icon = {
                    AnimatedContent(
                        targetState = isSelected,
                        label = "Selected icon transition",
                        transitionSpec = {
                            if (targetState) {
                                expandHorizontally(expandFrom = Alignment.Start) togetherWith fadeOut()
                            } else {
                                fadeIn() togetherWith fadeOut()
                            }
                        }
                    ) {
                        if (it) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        } else {
                            Icon(imageVector = authType.icon, contentDescription = null)
                        }
                    }

                },
                enabled = enabled
            ) {
                Text(stringResource(authType.labelRes))
            }
        }
    }
}

@Preview
@Composable
fun AuthTypeSelectorPreview() {
    var currentType by remember {
        mutableStateOf<AuthType>(AuthType.BasicAuth)
    }
    MaterialTheme {
        AuthTypeSelector(
            currentType = currentType,
            onAuthTypeChange = { currentType = it }
        )
    }
}
