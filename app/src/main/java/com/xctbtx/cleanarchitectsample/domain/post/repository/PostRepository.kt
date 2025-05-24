package com.xctbtx.cleanarchitectsample.domain.post.repository

import com.xctbtx.cleanarchitectsample.domain.post.model.Post

interface PostRepository {

    suspend fun getPosts(): List<Post>

}