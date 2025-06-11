package com.xctbtx.cleanarchitectsample.data.impl

import android.util.Log
import com.google.android.gms.common.api.Api
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.conversation.mapper.ConversationMapper.toConversationDto
import com.xctbtx.cleanarchitectsample.data.conversation.mapper.ConversationMapper.toConversationsDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.message.mapper.MessageMapper.toMessagesDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toUserDto
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toUsersDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreApiServiceImpl @Inject constructor(val api: FirebaseFirestore) :
    FireStoreApiService {
    private val registrations: MutableList<ListenerRegistration> = mutableListOf()
    override suspend fun getConversations(): List<ConversationDto> {
        val response = api.collection(Constants.CONVERSATION_PATH).get().await()
        return response.toConversationsDto()
    }

    override suspend fun getConversation(id: String): ConversationDto {
        val response = api.collection(Constants.CONVERSATION_PATH).document(id).get().await()
        return response.toConversationDto()
    }

    override fun syncConversations(onConversationChanged: (List<ConversationDto>) -> Unit) {
        val registration = api.collection(Constants.CONVERSATION_PATH)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                if (e != null) {
                    Log.d(TAG, "syncConversations: failed", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    onConversationChanged(snapshot.toConversationsDto())
                } else {
                    Log.d(TAG, "syncConversations: null")
                }
                for (change in snapshot!!.documentChanges) {
                    if (change.type == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "New Conversation: ${change.document.data}")
                    }
                    val source = if (snapshot.metadata.isFromCache) {
                        "local cache"
                    } else {
                        "server"
                    }
                    Log.d(TAG, "Data fetched from $source")
                }
            }
        registrations.add(registration)
    }

    override fun syncMessages(
        conversationId: String,
        onMessageChanged: (List<MessageDto>) -> Unit
    ) {
        val registration =
            api.collection(Constants.MESSAGE_PATH)
                .whereEqualTo("conversationId", conversationId)
                .orderBy(Constants.ORDER_BY_PARAM)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(TAG, "syncMessages: failed", e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        onMessageChanged(snapshot.toMessagesDto())
                    } else {
                        Log.d(TAG, "syncMessages: null")
                    }
                }
        registrations.add(registration)
    }

    override suspend fun getMessagesForConversation(conversationId: String): List<MessageDto> {
        val response =
            api.collection(Constants.MESSAGE_PATH).whereEqualTo("conversationId", conversationId)
                .orderBy(Constants.ORDER_BY_PARAM).get()
                .await()
        Log.d(TAG, "getMessages: ${response.size()}")
        return response.toMessagesDto()
    }

    override suspend fun getUser(id: String): UserDto {
        val response = api.collection(Constants.USER_PATH).document(id).get()
            .await()
        Log.d(TAG, "getUser: $response")
        return response.toUserDto()
    }


    override suspend fun login(username: String, password: String): UserDto? {
        val response = api.collection(Constants.USER_PATH)
            .whereEqualTo("username", username)
            .whereEqualTo("password", password)
            .limit(1)
            .get().await()
        return response.toUsersDto()?.first()
    }

    override suspend fun signIn(payload: UserDto, password: String, callBack: ApiCallBack) {
        val result = api.collection(Constants.USER_PATH).add(payload)
        api.collection(Constants.USER_PATH).document(result.await().id)
            .update("password", password).addOnCompleteListener {
                callBack.onSuccess()
                Log.d(TAG, "signIn: Success.")
            }
            .addOnFailureListener { e ->
                callBack.onFailure(
                    e.message ?: "Unknown error"
                )
                Log.d(TAG, "signIn: Failed")
            }
    }

    override fun sendMessage(payload: MessageDto, callBack: ApiCallBack) {
        api.collection(Constants.MESSAGE_PATH).add(payload)
            .addOnCompleteListener { id ->
                callBack.onSuccess()
                Log.d(TAG, "sendMessage $id Success.")
            }
            .addOnFailureListener { e ->
                callBack.onFailure(e.message ?: "Unknown error")
                Log.d(TAG, "sendMessage: Failed")
            }
    }

    override fun addConversation(payload: ConversationDto, callBack: ApiCallBack) {
        api.collection(Constants.CONVERSATION_PATH).add(payload)
            .addOnCompleteListener {
                callBack.onSuccess()
                Log.d(TAG, "addUser: Success.")
            }
            .addOnFailureListener { e ->
                callBack.onFailure(e.message ?: "Unknown error")
                Log.d(TAG, "addConversation: Failed")
            }
    }

    override fun detachAllListener() {
        registrations.forEach { it.remove() }
    }

    companion object {
        const val TAG = "FireStoreApiServiceImpl"
    }
}