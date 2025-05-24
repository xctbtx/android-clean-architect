package com.xctbtx.cleanarchitectsample.domain.message.model

data class Message(
    val id: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val senderId: String = ""
)