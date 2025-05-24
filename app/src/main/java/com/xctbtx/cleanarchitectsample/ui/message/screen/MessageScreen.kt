package com.xctbtx.cleanarchitectsample.ui.message.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.xctbtx.cleanarchitectsample.R
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel
import com.xctbtx.cleanarchitectsample.ui.message.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    navController: NavHostController,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Posts") })
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(text = state.error)
                }
            }

            else -> MessageList(messages = state.messages, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun MessageList(messages: List<MessageUiModel>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(messages) { message ->
            MessageItem(message)
            HorizontalDivider()
        }
    }
}

@Composable
fun MessageItem(message: MessageUiModel) {
    Row(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = message.image,
            contentDescription = "Ảnh từ internet",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Crop
        )
        //display message
        Text(text = message.content, style = MaterialTheme.typography.bodyMedium)
    }
}