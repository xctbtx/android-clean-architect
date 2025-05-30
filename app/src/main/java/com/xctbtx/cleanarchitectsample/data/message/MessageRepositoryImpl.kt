package com.xctbtx.cleanarchitectsample.data.message

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.message.mapper.MessageMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService,
) : MessageRepository {

    override suspend fun getMessages(conversationId: String): List<Message> {
        val response = api.getMessagesForConversation(conversationId)

        return response.map {
            it.toDomain()
        }
    }

    override fun syncMessages(
        conversationId: String,
        onMessageChanged: (List<Message>) -> Unit
    ) {
        api.syncMessages(conversationId) { messages ->
            val processedList = messages.map {
                it.toDomain()
            }
            onMessageChanged.invoke(processedList)
        }
    }

    override fun sendMessage(payload: MessageDto, apiCallBack: ApiCallBack) {
        api.sendMessage(payload, apiCallBack)
    }
}