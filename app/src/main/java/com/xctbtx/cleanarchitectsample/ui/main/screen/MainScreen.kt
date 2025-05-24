package com.xctbtx.cleanarchitectsample.ui.main.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.xctbtx.cleanarchitectsample.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Main Screen",
                    textAlign = TextAlign.Center
                )
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Welcome to the app!")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
                navController.navigate(Routes.POST)
            }) {
                Text("Go to Posts")
            }
            Button(onClick = {
                navController.navigate(Routes.CONVERSATION)
            }) {
                Text("Go to Chats")
            }
        }
    }
}
