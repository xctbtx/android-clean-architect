package com.xctbtx.cleanarchitectsample.ui.post.state

import com.xctbtx.cleanarchitectsample.domain.post.model.Post
import com.xctbtx.cleanarchitectsample.ui.common.state.CommonUiState

data class PostUiState(
    val posts: List<Post> = emptyList(),
    override val isLoading: Boolean = false,
    override val error: String? = null,
) : CommonUiState