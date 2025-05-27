package com.xctbtx.cleanarchitectsample.domain.message.usecase

import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.message.mapper.MessageMapper.toDto
import com.xctbtx.cleanarchitectsample.domain.message.model.Message
import com.xctbtx.cleanarchitectsample.domain.message.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepository,
) {
    operator fun invoke(payload: Message, apiCallBack: ApiCallBack) {
        return messageRepo.sendMessage(payload.toDto(), apiCallBack)
    }
}