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

    override fun syncConversations(onConversationChanged: (List<ConversationDto>) -> Unit) {
        api.collection(Constants.CONVERSATION_PATH)
            .addSnapshotListener { snapshot, exception ->
                if (exception == null && snapshot != null) {
                    onConversationChanged(snapshot.toObjects(ConversationDto::class.java))
                } else {
                    Log.d(TAG, "syncConversations: Error : ${exception?.message}")
                }
            }
    }

    override fun syncMessages(
        conversationId: String,
        onMessageChanged: (List<MessageDto>) -> Unit
    ) {
        api.collection(Constants.MESSAGE_PATH)
            .whereEqualTo("conversationId", conversationId)
            .orderBy(Constants.ORDER_BY_PARAM)
            .addSnapshotListener { snapshot, exception ->
                if (exception == null && snapshot != null) {
                    onMessageChanged(snapshot.toObjects(MessageDto::class.java))
                } else {
                    Log.d(TAG, "syncMessages: Error : ${exception?.message}")
                }
            }
    }

    override suspend fun getMessages(conversationId: String): List<MessageDto> {
        val response = api.collection(Constants.MESSAGE_PATH)
            .whereEqualTo("conversationId", conversationId)
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

    override fun addUser(payload: UserDto) {
        api.collection(Constants.USER_PATH).add(payload).addOnCompleteListener {
            Log.d(TAG, "addUser: Success.")
        }
    }

    override fun sendMessage(payload: MessageDto) {
        api.collection(Constants.MESSAGE_PATH).add(payload).addOnCompleteListener {
            Log.d(TAG, "addUser: Success.")
        }
    }

    override fun addConversation(payload: ConversationDto) {
        api.collection(Constants.CONVERSATION_PATH).add(payload).addOnCompleteListener {
            Log.d(TAG, "addUser: Success.")
        }
    }

    companion object {
        const val TAG = "FireStoreApiServiceImpl"
    }
}