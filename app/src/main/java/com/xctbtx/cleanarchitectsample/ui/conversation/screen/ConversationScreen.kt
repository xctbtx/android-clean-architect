package com.xctbtx.cleanarchitectsample.ui.conversation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.xctbtx.cleanarchitectsample.domain.conversation.model.Conversation
import com.xctbtx.cleanarchitectsample.ui.conversation.viewmodel.ConversationViewModel
import com.xctbtx.cleanarchitectsample.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    navController: NavHostController,
    viewModel: ConversationViewModel = hiltViewModel()
) {
    val state = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Conversations") })
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

            else -> ConversationList(
                navController = navController,
                conversations = state.conversations,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun ConversationList(
    navController: NavHostController,
    conversations: List<Conversation>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(conversations) { conversation ->
            ConversationItem(navController, conversation)
            HorizontalDivider()
        }
    }
}

@Composable
fun ConversationItem(navController: NavHostController, conversation: Conversation) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                navController.navigate(Routes.MESSAGE)
            }) {
        //display conversation
        //conversation.icon
        //conversation.unreadCount
        //conversation.updatedAt
        Text(text = conversation.title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = conversation.lastMessage, style = MaterialTheme.typography.bodyMedium)
    }
}