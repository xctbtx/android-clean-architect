package com.xctbtx.cleanarchitectsample.domain.conversation.usecase

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.domain.conversation.repository.ConversationRepository
import javax.inject.Inject

class GetConversationsUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(): List<Conversation> {
        return repository.getConversations()
    }
}