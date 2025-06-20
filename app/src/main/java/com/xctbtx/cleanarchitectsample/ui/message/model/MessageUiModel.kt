package com.xctbtx.cleanarchitectsample.ui.message.model

data class MessageUiModel(
    val id: String = "",
    val image: String = "",
    val content: String = "",
    val senderId: String? = null,
    val createdAt: String = "Just now",
)