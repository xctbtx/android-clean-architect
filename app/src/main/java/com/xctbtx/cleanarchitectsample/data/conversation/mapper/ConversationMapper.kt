package com.xctbtx.cleanarchitectsample.data.conversation.mapper

import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation

object ConversationMapper {
    fun ConversationDto.toDomain(): Conversation {
        return Conversation(
            id = this.id,
            title = this.title,
            icon = this.icon,
            participants = this.participants
        )
    }
}