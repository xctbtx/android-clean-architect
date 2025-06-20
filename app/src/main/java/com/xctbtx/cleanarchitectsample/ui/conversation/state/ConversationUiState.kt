package com.xctbtx.cleanarchitectsample.ui.conversation.state

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState

data class ConversationUiState(
    val conversations: List<Conversation> = listOf(),
    override val isLoading: Boolean = false,
    override val error: String? = null
) : CommonUiState
