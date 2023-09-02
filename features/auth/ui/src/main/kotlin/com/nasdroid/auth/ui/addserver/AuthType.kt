package com.nasdroid.auth.ui.addserver

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import com.nasdroid.auth.ui.R

/**
 * Various authenticastion types available to users.
 *
 * @property labelRes The String resource pointing to type title.
 * @property icon An icon representing the auth type.
 */
sealed class AuthType(
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    /**
     * Describes "basic" authentication, that is authentication via a username and password.
     */
    data object BasicAuth : AuthType(R.string.auth_type_basic, Icons.Default.Password)

    /**
     * Describes authentication via API Key.
     */
    data object ApiKeyAuth : AuthType(R.string.auth_type_key, Icons.Default.Key)

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