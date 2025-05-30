package com.xctbtx.cleanarchitectsample.data.message.mapper

import com.google.firebase.firestore.QuerySnapshot
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel

object MessageMapper {
    fun MessageDto.toDomain(): Message {
        return Message(
            id = this.id,
            content = this.content,
            createdAt = this.createdAt,
            senderId = this.senderId,
            conversationId = this.conversationId
        )
    }

    fun QuerySnapshot.toMessagesDto(): List<MessageDto> {
        return this.map { it.toObject(MessageDto::class.java).copy(id = it.id) }
    }

    fun Message.toDto(): MessageDto {
        return MessageDto(
            content = this.content,
            conversationId = this.conversationId,
            senderId = this.senderId,
        )
    }

    fun mapToUiModel(
        messages: List<Message>,
        avatarMap: Map<String, String>
    ): List<MessageUiModel> {
        return messages.map {
            MessageUiModel(
                id = it.id ?: "Error",
                image = avatarMap[it.senderId] ?: "",
                senderId = it.senderId,
                content = it.content,
                createdAt = it.toString(),
            )
        }
    }
}