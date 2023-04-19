package com.boswelja.truemanager.auth

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

internal sealed class AuthType(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    internal object BasicAuth : AuthType(R.string.username_label, Icons.Default.Password)
    internal object ApiKeyAuth : AuthType(R.string.password_label, Icons.Default.Key)

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
