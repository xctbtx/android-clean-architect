package com.xctbtx.cleanarchitectsample.data.conversation.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
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

    fun DocumentSnapshot.toConversationDto(): ConversationDto {
        return toObject(ConversationDto::class.java)?.copy(id = id) ?: ConversationDto()
    }

    fun QuerySnapshot.toConversationsDto(): List<ConversationDto> {
        return this.map { it.toObject(ConversationDto::class.java).copy(id = it.id) }
    }
}