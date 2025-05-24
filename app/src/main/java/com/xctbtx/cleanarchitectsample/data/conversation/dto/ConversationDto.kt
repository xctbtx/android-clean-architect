package com.xctbtx.cleanarchitectsample.data.conversation.dto

data class ConversationDto(
    val id: String = "",
    val title: String = "peer",
    val icon: String = "",
    val isGroup: Boolean = false,
    val participants: List<String> = listOf(),
    val nicknames: Map<String, String> = mapOf(),
    val messages: List<String> = listOf(),
    val lastMessageId: String = "IlWSxJjkZUnhL88gjDrE",
)