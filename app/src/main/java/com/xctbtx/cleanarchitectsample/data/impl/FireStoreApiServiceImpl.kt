package com.xctbtx.cleanarchitectsample.data.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.xctbtx.cleanarchitectsample.data.ApiCallBack
import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.conversation.dto.ConversationDto
import com.xctbtx.cleanarchitectsample.data.message.dto.MessageDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

class FireStoreApiServiceImpl @Inject constructor(val api: FirebaseFirestore) :
    FireStoreApiService {
    private val registrations: MutableList<ListenerRegistration> = mutableListOf()

    override suspend fun getConversations(): List<ConversationDto> {
        val response = api.collection(Constants.CONVERSATION_PATH)
            .get()
            .await()
        return response.map {
            it.toObject(ConversationDto::class.java).copy(id = it.id)
        }
    }

    override suspend fun getConversation(id: String): ConversationDto {
        val response = api.collection(Constants.CONVERSATION_PATH)
            .document(id)
            .get()
            .await()
        return response.toObject(ConversationDto::class.java)?.copy(id = response.id)
            ?: ConversationDto()
    }

    override fun syncConversations(onConversationChanged: (List<ConversationDto>) -> Unit) {
        val registration = api.collection(Constants.CONVERSATION_PATH)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d(TAG, "syncConversations: failed", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    onConversationChanged(snapshot.map {
                        it.toObject(ConversationDto::class.java).copy(id = it.id)
                    })
                } else {
                    Log.d(TAG, "syncConversations: null")
                }
            }
        registrations.add(registration)
    }

    override fun syncMessages(
        conversationId: String,
        onMessageChanged: (List<MessageDto>) -> Unit
    ) {
        val registration = api.collection(Constants.MESSAGE_PATH)
            .whereEqualTo("conversationId", conversationId)
            .orderBy(Constants.ORDER_BY_PARAM)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.d(TAG, "syncMessages: failed", e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    onMessageChanged(snapshot.map {
                        it.toObject(MessageDto::class.java).copy(
                            id = it.id
                        )
                    })
                } else {
                    Log.d(TAG, "syncMessages: null")
                }
            }
        registrations.add(registration)
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

    override fun addUser(payload: UserDto, callBack: ApiCallBack) {
        api.collection(Constants.USER_PATH).add(payload).addOnCompleteListener {
            callBack.onSuccess()
            Log.d(TAG, "addUser: Success.")
        }.addOnFailureListener { e ->
            callBack.onFailure(e.message ?: "Unknown error")
            Log.d(TAG, "addUser: Failed")
        }
    }

    override fun sendMessage(payload: MessageDto, callBack: ApiCallBack) {
        api.collection(Constants.MESSAGE_PATH).add(payload)
            .addOnCompleteListener { id ->
                callBack.onSuccess()
                Log.d(TAG, "sendMessage $id Success.")
            }.addOnFailureListener { e ->
                callBack.onFailure(e.message ?: "Unknown error")
                Log.d(TAG, "sendMessage: Failed")
            }
    }

    override fun addConversation(payload: ConversationDto, callBack: ApiCallBack) {
        api.collection(Constants.CONVERSATION_PATH).add(payload).addOnCompleteListener {
            callBack.onSuccess()
            Log.d(TAG, "addUser: Success.")
        }.addOnFailureListener { e ->
            callBack.onFailure(e.message ?: "Unknown error")
            Log.d(TAG, "addConversation: Failed")
        }
    }

    override fun detachAllListener() {
        registrations.forEach {
            it.remove()
        }
    }

    companion object {
        const val TAG = "FireStoreApiServiceImpl"
    }
}