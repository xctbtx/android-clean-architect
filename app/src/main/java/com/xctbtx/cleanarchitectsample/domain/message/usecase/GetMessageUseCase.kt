package com.xctbtx.cleanarchitectsample.domain.message.usecase

import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepository,
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(): List<MessageUiModel> {

        val item = messageRepo.getMessages()

        val result = item.map {
            val sender = userRepo.getUserById(it.senderId)
            MessageUiModel(
                id = it.id,
                image = sender.avatar,
                content = it.content,
                timestamp = it.timestamp
            )
        }

        return result
    }
}