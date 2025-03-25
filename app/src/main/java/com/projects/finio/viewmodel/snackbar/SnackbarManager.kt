package com.projects.finio.viewmodel.snackbar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SnackbarManager @Inject constructor() {
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearMessage() {
        _snackbarMessage.value = null
    }
}