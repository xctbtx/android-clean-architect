package com.xctbtx.cleanarchitectsample.data.message.mapper

import com.google.firebase.Timestamp
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

    fun Map<String, Any>.toMessageDto(): MessageDto {
        return MessageDto(
            id = this["id"] as String,
            content = this["content"] as String,
            createdAt = this["createdAt"] as Timestamp,
            senderId = this["senderId"] as String,
            conversationId = this["conversationId"] as String
        )
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