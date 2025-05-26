package com.xctbtx.cleanarchitectsample.domain.conversation.usecase

import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.domain.conversation.repository.ConversationRepository
import javax.inject.Inject

class SyncConversationUseCase @Inject constructor(
    private val repository: ConversationRepository
) {
    operator fun invoke(onConversationChanged: (List<Conversation>) -> Unit) {
        repository.syncConversations(onConversationChanged)
    }
}