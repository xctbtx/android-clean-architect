package com.xctbtx.cleanarchitectsample.data.message.dto

data class MessageDto(
    val id: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val senderId: String = ""
)
