package com.xctbtx.cleanarchitectsample.domain.post.usecase

import com.xctbtx.cleanarchitectsample.domain.post.model.Post
import com.xctbtx.cleanarchitectsample.domain.post.repository.PostRepository
import javax.inject.Inject


class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(): List<Post> {
        return repository.getPosts()
    }
}
