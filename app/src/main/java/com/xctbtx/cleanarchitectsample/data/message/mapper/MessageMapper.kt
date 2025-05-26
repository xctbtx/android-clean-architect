package com.xctbtx.cleanarchitectsample.data.message.mapper

import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel

object MessageMapper {
    fun MessageDto.toDomain(): Message {
        return Message(
            id = this.id,
            content = this.content,
            timestamp = this.timestamp,
            senderId = this.senderId
        )
    }

    fun Map<String, Any>.toMessageDto(): MessageDto {
        return MessageDto(
            id = this["id"] as String,
            content = this["content"] as String,
            timestamp = this["timestamp"] as Long,
            senderId = this["senderId"] as String
        )
    }

    fun mapToUiModel(messages: List<Message>, avatarMap: Map<String, String>): List<MessageUiModel> {
        return messages.map {
            MessageUiModel(
                id = it.id,
                image = avatarMap[it.senderId] ?: "",
                content = it.content,
                timestamp = it.timestamp
            )
        }
    }
}