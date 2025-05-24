package com.xctbtx.cleanarchitectsample.ui.post.state

import com.xctbtx.cleanarchitectsample.domain.post.model.Post

data class PostUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)