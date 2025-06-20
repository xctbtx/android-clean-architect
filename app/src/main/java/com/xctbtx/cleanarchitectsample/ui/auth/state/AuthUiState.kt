package com.xctbtx.cleanarchitectsample.ui.auth.state

import com.xctbtx.cleanarchitectsample.ui.auth.model.UserUiModel
import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState

data class AuthUiState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val isSuccess: Boolean = false,
    val isValidate: Boolean = false,
    val user: UserUiModel = UserUiModel(),
) : CommonUiState
