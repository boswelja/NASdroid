package com.nasdroid.auth.ui.register.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class AuthServerViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val serverAddress = savedStateHandle.get<String>("address")
}
