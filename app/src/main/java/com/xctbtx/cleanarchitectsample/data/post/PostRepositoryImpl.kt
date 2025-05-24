package com.xctbtx.cleanarchitectsample.data.post

import com.xctbtx.cleanarchitectsample.data.api.RetrofitApiService
import com.xctbtx.cleanarchitectsample.data.post.mapper.PostMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.post.model.Post
import com.xctbtx.cleanarchitectsample.domain.post.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        val response = api.getPosts()

        if (response.isSuccessful) {
            return response.body()?.map {
                it.toDomain()
            } ?: emptyList()
        } else {
            throw Exception("Failed to load posts: ${response.code()} ${response.message()}")
        }
    }
}