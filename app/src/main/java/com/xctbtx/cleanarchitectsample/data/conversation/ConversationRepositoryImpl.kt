package com.xctbtx.cleanarchitectsample.data.conversation

import android.util.Log
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.conversation.mapper.ConversationMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.domain.conversation.repository.ConversationRepository
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService,
) : ConversationRepository {

    override suspend fun getConversations(): List<Conversation> {
        val response = api.getConversations()
        Log.d("DungVT18", "getConversations: ${response[0].id}")
        return response.map {
            it.toDomain()
        }
    }

    override suspend fun getConversation(id: String): Conversation {
        val response = api.getConversation(id)
        return response.toDomain()
    }

    override fun syncConversations(onConversationChanged: (List<Conversation>) -> Unit) {
        api.syncConversations { list ->
            val processedList = list.map {
                it.toDomain()
            }
            onConversationChanged.invoke(processedList)
        }
    }
}