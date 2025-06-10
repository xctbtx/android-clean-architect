package com.xctbtx.cleanarchitectsample.di

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.persistentCacheSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore =
        FirebaseFirestore.getInstance().apply {
            //set cache to 200MB
            val settings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(persistentCacheSettings { setSizeBytes(200 * 1024 * 1024) })
                .build()
            firestoreSettings = settings
            //enable indexing in cache
            Firebase.firestore.persistentCacheIndexManager?.apply {
                // Indexing is disabled by default
                enableIndexAutoCreation()
            } ?: Log.d("FirebaseModule","indexManager is null")
        }
}