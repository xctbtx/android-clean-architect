package com.xctbtx.cleanarchitectsample.data.api

import com.xctbtx.cleanarchitectsample.data.Constants
import com.xctbtx.cleanarchitectsample.data.post.dto.PostDto
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApiService {

    @GET(Constants.POST_ENDPOINT)
    suspend fun getPosts(): Response<List<PostDto>>

    @GET(Constants.USER_ENDPOINT)
    suspend fun getUser(id: String): Response<UserDto>
}