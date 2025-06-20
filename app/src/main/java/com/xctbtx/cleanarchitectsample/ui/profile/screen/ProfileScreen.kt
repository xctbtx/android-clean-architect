package com.xctbtx.cleanarchitectsample.ui.profile.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.xctbtx.cleanarchitectsample.data.Cache
import com.xctbtx.cleanarchitectsample.ui.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navigateToLogin: () -> Unit) {
    if (Cache.currentUserId != null) {
        ProfileContainer()
    } else {
        navigateToLogin()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContainer() {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Register") })
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.error)
                }
            }

            else -> {
                padding
            }
        }
    }
}