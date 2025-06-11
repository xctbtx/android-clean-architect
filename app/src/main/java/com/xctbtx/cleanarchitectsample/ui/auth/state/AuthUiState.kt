package com.xctbtx.cleanarchitectsample.ui.auth.state

import com.xctbtx.cleanarchitectsample.ui.auth.model.UserUiModel

data class AuthUiState(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: UserUiModel = UserUiModel()
)
