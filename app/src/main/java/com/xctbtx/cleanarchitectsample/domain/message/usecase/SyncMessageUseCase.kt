package com.xctbtx.cleanarchitectsample.domain.message.usecase

import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import javax.inject.Inject

class SyncMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepository,
) {
    operator fun invoke(conversationId: String, onMessageChanged: (List<Message>) -> Unit) {
        messageRepo.syncMessages(conversationId, onMessageChanged)
    }
}