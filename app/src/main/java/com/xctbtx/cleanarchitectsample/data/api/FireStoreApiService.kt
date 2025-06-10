package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto

interface FireStoreApiService {
    fun detachAllListener()
    suspend fun getConversations(): List<ConversationDto>
    suspend fun getConversation(id: String): ConversationDto
    fun syncConversations(onConversationChanged: (List<ConversationDto>) -> Unit)
    suspend fun getMessagesForConversation(conversationId: String): List<MessageDto>
    fun syncMessages(conversationId: String, onMessageChanged: (List<MessageDto>) -> Unit)
    suspend fun getUser(id: String): UserDto
    suspend fun login(username: String, password: String): UserDto?
    suspend fun signIn(payload: UserDto, password: String, callBack: ApiCallBack)
    fun sendMessage(payload: MessageDto, callBack: ApiCallBack)
    fun addConversation(payload: ConversationDto, callBack: ApiCallBack)
}