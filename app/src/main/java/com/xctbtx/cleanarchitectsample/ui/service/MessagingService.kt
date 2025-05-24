package com.xctbtx.cleanarchitectsample.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    val TAG = "MessagingService"
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: ${message.data}")
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "onNewToken: $token")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "firebase-messaging",
                "Firebase Messaging Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}