package com.xctbtx.cleanarchitectsample.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xctbtx.cleanarchitectsample.domain.post.usecase.GetPostsUseCase
import com.xctbtx.cleanarchitectsample.ui.post.state.PostUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(PostUiState())
        private set

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val posts = getPostsUseCase()
                uiState = uiState.copy(posts = posts, isLoading = false, error = null)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.message ?: "Unknown error", isLoading = false)
            }
        }
    }
}
