package com.xctbtx.cleanarchitectsample.ui.message.state

import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel

data class MessageUiState(
    val title: String = "",
    val messages: List<MessageUiModel> = listOf(),
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : CommonUiState