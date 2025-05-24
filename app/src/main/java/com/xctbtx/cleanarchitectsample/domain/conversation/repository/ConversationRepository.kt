package com.xctbtx.cleanarchitectsample.domain.conversation.repository

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation


interface ConversationRepository {
    suspend fun getConversations(): List<Conversation>
}