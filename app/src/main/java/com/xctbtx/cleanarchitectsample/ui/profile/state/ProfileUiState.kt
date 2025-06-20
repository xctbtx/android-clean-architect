package com.xctbtx.cleanarchitectsample.ui.profile.state

import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState

data class ProfileUiState(
    val isSuccess: Boolean = false,
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : CommonUiState