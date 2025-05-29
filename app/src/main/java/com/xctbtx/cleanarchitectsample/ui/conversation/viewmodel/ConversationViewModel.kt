package com.xctbtx.cleanarchitectsample.ui.conversation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.domain.conversation.usecase.GetConversationsUseCase
import com.xctbtx.cleanarchitectsample.domain.conversation.usecase.SyncConversationUseCase
import com.xctbtx.cleanarchitectsample.ui.conversation.state.ConversationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getConversationsUseCase: GetConversationsUseCase,
    private val syncConversationUseCase: SyncConversationUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ConversationUiState())
        private set

    init {
        loadConversations()
        syncConversations()
    }

    private fun syncConversations() {
        syncConversationUseCase(::onConversationChanged)
    }

    private fun loadConversations() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val conversations = getConversationsUseCase()
                Log.d(TAG, "loadConversations: $conversations")
                uiState =
                    uiState.copy(conversations = conversations, isLoading = false, error = null)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    private fun onConversationChanged(data: List<Conversation>) {
        Log.d(TAG, "onConversationChanged: ${data.size}")
        uiState = uiState.copy(conversations = data, isLoading = false, error = null)
    }

    companion object {
        const val TAG = "ConversationViewModel"
    }
}
