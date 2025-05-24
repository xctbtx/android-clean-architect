package com.xctbtx.cleanarchitectsample.ui.message.state

import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel

data class MessageUiState(
    val messages: List<MessageUiModel> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)