package com.xctbtx.cleanarchitectsample.ui.message.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.xctbtx.cleanarchitectsample.R
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel
import com.xctbtx.cleanarchitectsample.ui.message.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel()
) {
    val conversation =
        rememberNavController().previousBackStackEntry?.savedStateHandle?.get<Conversation>("conversation")
    LaunchedEffect(conversation?.id) {
        viewModel.loadMessages(conversation?.id ?: "", conversation?.participants ?: listOf())
    }
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

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(Modifier.padding(padding))
                ) {
                    MessageList(
                        messages = state.messages,
                        modifier = Modifier
                            .weight(9f)
                            .fillMaxWidth()
                    )
                    ChatBox(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MessageList(messages: List<MessageUiModel>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
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
                .width(50.dp)
                .height(50.dp),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.FillBounds
        )
        //display message
        Text(text = message.content, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ChatBox(viewModel: MessageViewModel = hiltViewModel(), modifier: Modifier) {
    Row {
        TextField(viewModel.messageContent, viewModel::onMessageChange)
        Button(onClick = {
            viewModel.sendMessage()
        }) { Text("Send") }
    }
}