package com.xctbtx.cleanarchitectsample.data.user

import com.xctbtx.cleanarchitectsample.data.api.RetrofitApiService
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.user.model.User
import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: RetrofitApiService,
) : UserRepository {

    override suspend fun getUserById(id: String): User {
        val response = api.getUser(id)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomain()
        } else {
            throw Exception("Failed to load user : ${response.code()} ${response.message()}")
        }
    }
}