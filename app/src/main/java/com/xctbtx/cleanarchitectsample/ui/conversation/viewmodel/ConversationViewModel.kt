package com.xctbtx.cleanarchitectsample.ui.conversation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.domain.conversation.usecase.GetConversationUseCase
import com.xctbtx.cleanarchitectsample.ui.conversation.state.ConversationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val getConversationUseCase: GetConversationUseCase
) : ViewModel() {

    var uiState by mutableStateOf(ConversationUiState())
        private set

    init {
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val conversations = getConversationUseCase()
                Log.d("TAG", "loadConversations: $conversations")
                uiState = uiState.copy(conversations = conversations, isLoading = false, error = null)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}
