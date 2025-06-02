package com.xctbtx.cleanarchitectsample.ui.auth.state

data class LoginUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
