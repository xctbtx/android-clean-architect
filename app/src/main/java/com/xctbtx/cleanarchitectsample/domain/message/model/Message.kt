package com.xctbtx.cleanarchitectsample.domain.message.model

import com.google.firebase.Timestamp

data class Message(
    val id: String? = null,
    val content: String = "",
    val createdAt: Timestamp? = null,
    val conversationId: String = "",
    val senderId: String? = null
)