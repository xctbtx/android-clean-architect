package com.xctbtx.cleanarchitectsample.domain.conversation.usecase

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.domain.conversation.repository.ConversationRepository
import javax.inject.Inject

class GetConversationUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    suspend operator fun invoke(id: String): Conversation {
        return repository.getConversation(id)
    }
}