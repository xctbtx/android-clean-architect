package com.xctbtx.cleanarchitectsample.ui.message.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.message.mapper.MessageMapper
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.usecase.GetMessageUseCase
import com.xctbtx.cleanarchitectsample.domain.message.usecase.SendMessageUseCase
import com.xctbtx.cleanarchitectsample.domain.message.usecase.SyncMessageUseCase
import com.xctbtx.cleanarchitectsample.domain.user.usecase.GetUserAvatarUseCase
import com.xctbtx.cleanarchitectsample.ui.conversation.viewmodel.ConversationViewModel.Companion.TAG
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel
import com.xctbtx.cleanarchitectsample.ui.message.state.MessageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getMessageUseCase: GetMessageUseCase,
    private val syncMessageUseCase: SyncMessageUseCase,
    private val getUserAvatar: GetUserAvatarUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
) : ViewModel() {
    //todo Need login function for this to be real
    private val currentUser = "user1"

    var messageContent by mutableStateOf("")
        private set

    fun onMessageChange(newValue: String) {
        messageContent = newValue
    }

    private var avatarMap = mapOf<String, String>()

    var uiState by mutableStateOf(MessageUiState())
        private set

    fun loadMessages(conversationId: String, participants: List<String>) {
        Log.d(TAG, "loadMessages: $conversationId")
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val messages = getMessageUseCase(conversationId)
                avatarMap = participants.associateWith { userId ->
                    async { getUserAvatar(userId) }
                }.mapValues { it.value.await() }
                val result = MessageMapper.mapToUiModel(messages, avatarMap)
                uiState = uiState.copy(messages = result, isLoading = false, error = null)
                syncMessage(conversationId)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }

    private fun syncMessage(conversationId: String) {
        viewModelScope.launch {
            syncMessageUseCase(conversationId) { list ->
                onMessageChanged(MessageMapper.mapToUiModel(list, avatarMap))
            }
        }
    }

    private fun onMessageChanged(data: List<MessageUiModel>) {
        Log.d(TAG, "onMessageChanged: ${data.size}")
        uiState = uiState.copy(messages = data, isLoading = false, error = null)
    }

    fun sendMessage() {
        //send message
        sendMessageUseCase(
            Message(content = messageContent, senderId = currentUser),
            apiCallBack = object : ApiCallBack {
                override fun onSuccess() {
                    Log.d(TAG, "sendMessage onSuccess")
                }

                override fun onFailure(error: String) {
                    Log.d(TAG, "sendMessage onFailure: $error")
                }

            }
        )
        messageContent = ""
    }
}
