package com.boswelja.truemanager.auth.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import com.boswelja.truemanager.auth.R

sealed class AuthType(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    object BasicAuth : AuthType(R.string.auth_type_basic, Icons.Default.Password)
    object ApiKeyAuth : AuthType(R.string.auth_type_key, Icons.Default.Key)

}

internal val AuthTypes = listOf(
    AuthType.BasicAuth,
    AuthType.ApiKeyAuth
)

@Composable
internal fun rememberAuthTypeSaver(): Saver<MutableState<AuthType>, Int> {
    return Saver(
        save = {
            AuthTypes.indexOf(it.value)
        },
        restore = {
            mutableStateOf(AuthTypes[it])
        }
    )
}
