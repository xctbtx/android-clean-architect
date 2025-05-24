package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto

interface FireStoreApiService {

    suspend fun getConversations(): List<ConversationDto>
    suspend fun getMessages(): List<MessageDto>
    suspend fun getUser(id: String): UserDto
}