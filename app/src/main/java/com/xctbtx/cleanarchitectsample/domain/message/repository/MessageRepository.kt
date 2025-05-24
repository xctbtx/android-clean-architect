package com.xctbtx.cleanarchitectsample.domain.message.repository

import com.xctbtx.cleanarchitectsample.domain.message.model.Message

interface MessageRepository {
    suspend fun getMessages(): List<Message>
}