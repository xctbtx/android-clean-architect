package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.post.dto.PostDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface RetrofitApiService {

    @GET(Constants.POST_ENDPOINT)
    suspend fun getPosts(): Response<List<PostDto>>

    @PUT(Constants.POST_ENDPOINT)
    suspend fun addPost(@Body payload: PostDto)
}