package com.xctbtx.cleanarchitectsample.ui.message.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.domain.message.usecase.GetMessageUseCase
import com.xctbtx.cleanarchitectsample.ui.message.state.MessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase
) : ViewModel() {
    var messageContent by mutableStateOf("")
        private set

    fun onMessageChange(newValue: String) {
        messageContent = newValue
    }

    var uiState by mutableStateOf(MessageUiState())
        private set

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val messages = getMessageUseCase()
                uiState = uiState.copy(messages = messages, isLoading = false, error = null)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    fun sendMessage() {
        //send message
        messageContent = ""
    }
}
