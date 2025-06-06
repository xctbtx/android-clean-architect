package com.xctbtx.cleanarchitectsample.domain.message.usecase

import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String): List<Message> {
        return messageRepo.getMessages(conversationId)
    }
}