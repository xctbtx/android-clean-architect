package com.xctbtx.cleanarchitectsample.ui.conversation.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.xctbtx.cleanarchitectsample.R
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.ui.conversation.viewmodel.ConversationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    navigateToMessage: (conversationId: String) -> Unit,
    navigateToNewConversation: () -> Unit,
) {
    val viewModel: ConversationViewModel = hiltViewModel()
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Conversations") })
        }, floatingActionButton = {
            if (!state.isLoading && state.error == null) {
                FloatingActionButton(
                    onClick = { navigateToNewConversation() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
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
                ConversationList(
                    navigateToMessage,
                    conversations = state.conversations,
                    modifier = Modifier.padding(padding)
                )
            }
        }

    }
}

@Composable
fun ConversationList(
    navigateToMessage: (String) -> Unit,
    conversations: List<Conversation>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(conversations) { conversation ->
            ConversationItem(navigateToMessage, conversation)
            HorizontalDivider()
        }
    }
}

@Composable
fun ConversationItem(navigateToMessage: (String) -> Unit, conversation: Conversation) {
    val unread = conversation.unreadCount
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                Log.d("TAG", "ConversationItem: $conversation")
                navigateToMessage(conversation.id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1.8f)) {
            AsyncImage(
                model = conversation.icon,
                contentDescription = "Conversation icon",
                modifier = Modifier
                    .clip(CircleShape)
                    .width(40.dp)
                    .height(40.dp),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Crop
            )
        }
        Box(modifier = Modifier.weight(8f)) {
            Column {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (unread > 0) FontWeight.SemiBold else FontWeight.Normal)
                )
            }
        }
        if (unread > 0) Badge(modifier = Modifier.weight(0.9f))
        {
            Text(if (unread > 99) "99+" else unread.toString())
        }
    }
}