package com.xctbtx.cleanarchitectsample.data.message

import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.message.mapper.MessageMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService,
) : MessageRepository {

    override suspend fun getMessages(): List<Message> {
        val response = api.getMessages()

        return response.map {
            it.toDomain()
        }
    }
}