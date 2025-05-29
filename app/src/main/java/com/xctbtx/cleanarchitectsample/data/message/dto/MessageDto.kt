package com.xctbtx.cleanarchitectsample.data.message.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class MessageDto(
    @get:Exclude
    val id: String? = null,
    val content: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    @get:ServerTimestamp
    var createdAt: Timestamp? = null
)
