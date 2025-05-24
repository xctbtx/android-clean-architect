package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto

interface FireStoreApiService {

    suspend fun getConversations(): List<ConversationDto>
    suspend fun getMessages(): List<MessageDto>
}