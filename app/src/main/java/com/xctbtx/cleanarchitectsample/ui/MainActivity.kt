package com.xctbtx.cleanarchitectsample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.ui.main.screen.MainScaffold
import com.xctbtx.cleanarchitectsample.ui.theme.CleanArchitectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firestore: FireStoreApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CleanArchitectTheme {
                MainScaffold()
            }
        }
    }

    override fun onDestroy() {
        firestore.detachAllListener()
        super.onDestroy()
    }
}