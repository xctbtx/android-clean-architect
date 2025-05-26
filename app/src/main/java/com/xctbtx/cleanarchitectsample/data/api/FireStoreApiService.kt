package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto

interface FireStoreApiService {

    suspend fun getConversations(): List<ConversationDto>
    fun syncConversations(onConversationChanged: (List<ConversationDto>) -> Unit)
    suspend fun getMessages(conversationId: String): List<MessageDto>
    fun syncMessages(conversationId: String, onMessageChanged: (List<MessageDto>) -> Unit)
    suspend fun getUser(id: String): UserDto
    fun addUser(payload: UserDto)
    fun sendMessage(payload: MessageDto)
    fun addConversation(payload: ConversationDto)
}