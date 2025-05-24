package com.xctbtx.cleanarchitectsample.ui.post.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.xctbtx.cleanarchitectsample.domain.post.model.Post
import com.xctbtx.cleanarchitectsample.ui.post.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel = hiltViewModel()
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

            else -> PostList(posts = state.posts, modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun PostList(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(posts) { post ->
            PostItem(post)
            HorizontalDivider()
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(model = post.imageUrl, contentDescription = "post image")
        Text(text = post.title ?: "", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = post.content ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}