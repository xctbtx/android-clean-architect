package com.xctbtx.cleanarchitectsample.domain.conversation.model

data class Conversation(
    val id: String = "",
    val title: String = "peer",
    val icon: String = "",
    val lastMessage: String = "preview",
    val participants: List<String> = listOf(),
    val updatedAt: Long = 0,
    val unreadCount: Int = 0,
)