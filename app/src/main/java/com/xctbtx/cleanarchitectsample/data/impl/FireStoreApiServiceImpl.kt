package com.xctbtx.cleanarchitectsample.data.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreApiServiceImpl @Inject constructor(val api: FirebaseFirestore) :
    FireStoreApiService {
    override suspend fun getConversations(): List<ConversationDto> {
        val response = api.collection(Constants.CONVERSATION_PATH)
            .get()
            .await()
        Log.d(TAG, "getConversations: ${response.size()}")
        return response.toObjects(ConversationDto::class.java)
    }

    override suspend fun getMessages(): List<MessageDto> {
        val response = api.collection(Constants.MESSAGE_PATH)
            .orderBy(Constants.ORDER_BY_PARAM)
            .get()
            .await()
        Log.d(TAG, "getMessages: ${response.size()}")
        return response.toObjects(MessageDto::class.java)
    }

    override suspend fun getUser(id: String): UserDto {
        val response = api.collection(Constants.USER_PATH)
            .document(id)
            .get()
            .await()
        Log.d(TAG, "getUser: $response")
        return response.toObject(UserDto::class.java) ?: UserDto()
    }

    companion object {
        const val TAG = "FireStoreApiServiceImpl"
    }
}