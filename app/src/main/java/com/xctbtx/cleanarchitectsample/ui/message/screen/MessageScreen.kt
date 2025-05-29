package com.xctbtx.cleanarchitectsample.ui.message.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.xctbtx.cleanarchitectsample.R
import com.xctbtx.cleanarchitectsample.ui.message.model.MessageUiModel
import com.xctbtx.cleanarchitectsample.ui.message.viewmodel.MessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    conversationId: String? = null,
) {
    val TAG = "MessageScreen"
    val viewModel: MessageViewModel = hiltViewModel()
    val nav: NavHostController = rememberNavController()

    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId ?: "")
    }
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Message",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.popBackStack()
                        Log.d(TAG, "MessageScreen: back clicked")
                    }) {
                        Image(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    MessageList(
                        messages = state.messages,
                        currentUserId = viewModel.currentUserId,
                        modifier = Modifier.weight(9f)
                    )
                    ChatBox(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun MessageList(
    messages: List<MessageUiModel>,
    currentUserId: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(messages) { message ->
            val isMyOwn = currentUserId == message.senderId
            Box(
                contentAlignment = if (isMyOwn) Alignment.CenterEnd else Alignment.CenterStart,
                modifier = modifier.fillMaxSize()
            ) {
                MessageItem(message, isMyOwn, modifier)
            }
        }
    }
}

@Composable
fun MessageItem(message: MessageUiModel, isMyOwn: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(4.dp),
        horizontalArrangement = if (isMyOwn) Arrangement.End else Arrangement.Start,
    ) {
        if (!isMyOwn) AsyncImage(
            model = message.image,
            contentDescription = "Ảnh từ internet",
            modifier = Modifier
                .clip(CircleShape)
                .width(40.dp)
                .height(40.dp),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            error = painterResource(R.drawable.ic_launcher_foreground),
            contentScale = ContentScale.Crop
        )
        VerticalDivider(thickness = 4.dp)
        Text(
            modifier = Modifier
                .background(
                    if (isMyOwn) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(10.dp),
            text = message.content,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun ChatBox(modifier: Modifier) {
    val viewModel: MessageViewModel = hiltViewModel()
    Row(modifier = modifier.padding(bottom = 10.dp)) {
        TextField(
            value = viewModel.messageContent,
            colors = TextFieldDefaults.colors().copy(
                errorContainerColor = Color.Red,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            onValueChange = viewModel::onMessageChange,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(4.dp)
                .weight(8f),
        )
        Image(
            Icons.AutoMirrored.Filled.Send,
            null,
            Modifier
                .padding(4.dp)
                .clickable {
                    viewModel.sendMessage()
                }
                .align(Alignment.CenterVertically)
        )
    }
}