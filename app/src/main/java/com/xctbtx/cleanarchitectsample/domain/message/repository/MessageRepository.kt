package com.xctbtx.cleanarchitectsample.domain.message.repository

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.domain.message.model.Message

interface MessageRepository {
    suspend fun getMessages(conversationId: String): List<Message>
    fun syncMessages(conversationId: String, onMessageChanged: (List<Message>) -> Unit)
    fun sendMessage(payload: MessageDto, apiCallBack: ApiCallBack)
}