package com.xctbtx.cleanarchitectsample.data.message.dto

data class MessageDto(
    val id: String = "",
    val content: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val timestamp: Long = 0
)
