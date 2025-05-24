package com.xctbtx.cleanarchitectsample.ui.conversation.state

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation

data class ConversationUiState(
    val conversations: List<Conversation> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null
)