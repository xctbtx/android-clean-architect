package com.xctbtx.cleanarchitectsample.ui.menu.state

import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState

class MenuUiState(
    override val isLoading: Boolean = false,
    override val error: String? = null
) : CommonUiState