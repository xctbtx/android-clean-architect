package com.xctbtx.cleanarchitectsample.data.conversation

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

        return response.map {
            it.toDomain()
        }
    }
}