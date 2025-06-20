package com.xctbtx.cleanarchitectsample.ui.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.xctbtx.cleanarchitectsample.R
import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import javax.inject.Inject
import kotlin.random.Random

class FireStoreService @Inject constructor(private val apiService: FireStoreApiService) :
    Service() {

    inner class FireStoreBinder : Binder() {
        fun getService(): FireStoreService = this@FireStoreService
    }

    override fun onBind(intent: Intent?): IBinder {
        return FireStoreBinder()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(
            Constants.DEFAULT_NOTIFICATION_ID,
            getNotification(
                "CleanArchitect",
                "Firebase messaging is running in background",
                false
            ).build()
        )
        return START_STICKY
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun startListenToServer(conversationIds: List<String>) {
        conversationIds.forEach { id ->
            apiService.syncMessages(id) { message ->
                message.filter { !it.isSeen }.forEach { mess ->
                    NotificationManagerCompat.from(this).notify(
                        Random.nextInt(1000),
                        getNotification(
                            title = mess.senderId,
                            content = mess.content,
                            autoCancel = true
                        ).build()
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getNotification(
        title: String?,
        content: String?,
        autoCancel: Boolean = false
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(autoCancel)
    }
}